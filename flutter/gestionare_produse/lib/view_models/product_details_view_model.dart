import 'package:gestionare_produse/domain/product.dart';
import 'package:gestionare_produse/repository/products_repository.dart';

class ProductDetailsViewModel{

  final ProductRepository _repo;

  ProductDetailsViewModel(this._repo);

  void updateProduct(Product product) {
    _repo.updateProduct(product);
  }

  void deleteProduct(Product product) {
    _repo.deleteProduct(product);
  }

  String? validateName(String? name){
    if (name!.isEmpty) {
      return "Name is required";
    }
    if (name.length > 70) {
      return "Name must not exceed 70 characters";
    }
    return null;
  }

  String? validateBrand(String? brand){
    if (brand!.isEmpty) {
      return "Brand is required";
    }
    if (brand.length > 30) {
      return "Brand must not exceed 30 characters";
    }
    return null;
  }

  String? validatePrice(String? price){
    if (price!.isEmpty) {
      return "Price is required";
    }
    try{
      double pr = double.parse(price);
    } on FormatException{
      return "You must enter a valid price";
    }
    return null;
  }

  String? validateExpirationDate(String? expDate){
    if (expDate!.isEmpty) {
      return "Expiration date is required";
    }
    return null;
  }
}