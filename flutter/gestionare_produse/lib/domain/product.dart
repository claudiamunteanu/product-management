class Product{
  late int id;
  late String name;
  late String brand;
  late double price;
  late bool isPerUnit;
  late DateTime expirationDate;
  late bool isRefrigerated;
  late String image;
  late int warehouseId;

  Product({required this.id, required this.name, required this.brand, required this.price, required this.isPerUnit,
      required this.expirationDate, required this.isRefrigerated, required this.image, required this.warehouseId});
}