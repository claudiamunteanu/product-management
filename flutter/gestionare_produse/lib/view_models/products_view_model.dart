import 'package:flutter/material.dart';
import 'package:gestionare_produse/domain/product.dart';
import 'package:gestionare_produse/repository/products_repository.dart';

class ProductsViewModel{

  final ProductRepository _repo;

  ProductsViewModel(this._repo);

  List<Product> getAllProducts() {
    return _repo.getAllProducts();
  }
}