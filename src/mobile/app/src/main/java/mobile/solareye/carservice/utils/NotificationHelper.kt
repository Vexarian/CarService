package mobile.solareye.carservice.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import mobile.solareye.carservice.R
import mobile.solareye.carservice.data.model.Order

private val CHANNEL_ID = "Заказы, заведенные больше, чем 1 день назад"

fun showNotificationsIfNeeded(
    context: Context,
    ordersForNotifications: List<Order>,
) {

    createNotificationChannel(context)

    ordersForNotifications.forEachIndexed { index, order ->
        showNotification(
            context = context,
            order = order,
            isGroupSummary = index == ordersForNotifications.lastIndex,
        )
    }
}

private fun createNotificationChannel(
    context: Context,
) {
    val channel = NotificationChannelCompat.Builder(
        CHANNEL_ID,
        NotificationManagerCompat.IMPORTANCE_DEFAULT,
    ).apply {
        setName(CHANNEL_ID)
    }.build()

    NotificationManagerCompat.from(context).createNotificationChannel(channel)
}

private fun showNotification(
    context: Context,
    order: Order,
    isGroupSummary: Boolean,
) {
    val GROUP_KEY_WORK_EMAIL = "mobile.solareye.carservice.ORDERS_OLDER_THAN_ONE_DAY"

    val newMessageNotification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentIntent(
            navPendingIntent(
                context,
                order.orderId,
            )
        )
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Заказ ${order.orderId}")
        .setContentText("${order.carName} ${order.carLicensePlate}")
        .setGroup(GROUP_KEY_WORK_EMAIL)
        .setGroupSummary(isGroupSummary)
        .build()

    NotificationManagerCompat.from(context).apply {
        notify(order.orderId, newMessageNotification)
    }

}

private val REQUEST_CODE = 1010

const val orderUri = "app://mobile.solareye/order_show?orderId="

private fun navPendingIntent(
    context: Context,
    orderId: Int,
): PendingIntent? = TaskStackBuilder.create(context).run {
    val intent = Intent(
        Intent.ACTION_VIEW,
        "$orderUri$orderId".toUri()
    )
    addNextIntentWithParentStack(
        intent
    )
    getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
}