package app.gestionareproduse.products.domain

enum class SortField(val value: String){
    EXP_DATE("Expiration Date"),
    NAME("Name"),
    BRAND("Brand"),
    PRICE("Price")
}

fun getAllSortFields(): List<SortField>{
    return listOf(SortField.EXP_DATE, SortField.NAME, SortField.BRAND, SortField.PRICE)
}

fun getSortField(value: String): SortField? {
    val map = SortField.values().associateBy(SortField::value)
    return map[value]
}