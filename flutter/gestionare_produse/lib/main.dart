import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:gestionare_produse/repository/products_repository.dart';
import 'package:gestionare_produse/screens/new_product.dart';
import 'package:gestionare_produse/screens/product_details.dart';
import 'package:gestionare_produse/screens/products_screen.dart';
import 'package:gestionare_produse/view_models/new_product_view_model.dart';
import 'package:provider/provider.dart';

void main() {
  ProductRepository repo = new ProductRepository();
  runApp(MaterialApp(
        initialRoute: '/',
        routes: {
          '/': (context) => ProductsScreen(repo),
          '/details' : (context) => ProductDetails(repo),
          '/newProduct' : (context) => NewProduct(repo)
        }
    ),
  );
}