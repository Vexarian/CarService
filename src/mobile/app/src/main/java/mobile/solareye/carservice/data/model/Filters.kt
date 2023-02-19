package mobile.solareye.carservice.data.model

enum class StatusFilter(val title: String) {
    ACTIVE("Активный"),
    ALL("Все"),
    ;

    companion object {
        fun getFilterByTitle(title: String) = values().firstOrNull { it.title == title }
    }
}