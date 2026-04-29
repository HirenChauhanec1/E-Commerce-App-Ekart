package com.codewithhiren.ekart.utils

enum class FireStoreCollection {
    RegisteredUsers2,
    Products,
    User,
    UserHavingOrder
}

enum class UserSubCollection {
    Cart,
    Address,
    Orders
}

enum class Category(val category: String) {
    Chair("Chair"),
    Cupboard("Cupboard"),
    Table("Table"),
    Electronics("Electronics"),
    Furniture("Furniture"),
    BestDeals("Best Deals"),
    BestProducts("Best Products"),
    SpecialProducts("Special Products")
}

enum class MainCategory {
    Chair,
    Cupboard,
    Table,
    Electronics,
    Furniture
}

enum class CartProductEnum {
    selectedColor,
    selectedSize,
    quantity
}

enum class OrderStatus {
    Ordered,
    Confirmed,
    Shipped,
    Delivered,
    Canceled,
    Returned
}


