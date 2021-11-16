import 'dart:collection';

import 'package:flutter/cupertino.dart';
import 'package:gestionare_produse/domain/product.dart';
import 'package:gestionare_produse/utils/utils.dart';

class ProductRepository{
  final List<Product> _products = [
    Product(
        id: 1,
        name: "Iaurt cu cereale, nuci si ovaz",
        brand: "Activia",
        price: 1.80,
        isPerUnit: true,
        expirationDate: stringToDate("16.10.2021"),
        isRefrigerated: true,
        image:
        "https://www.auchan.ro/public/images/h33/hd1/h00/iaurt-activia-cu-cereale-nuci-si-ovaz-125g-8950867689502.jpg",
        warehouseId: 1),
    Product(
        id: 2,
        name: "Biscuiti sarati, originali",
        brand: "Tuc",
        price: 2.55,
        isPerUnit: true,
        expirationDate: stringToDate("14.11.2021"),
        isRefrigerated: false,
        image:
        "https://www.auchan.ro/public/images/h01/hb2/h00/biscuiti-tuc-originali-100-g-8861037395998.jpg",
        warehouseId: 1),
    Product(
        id: 3,
        name: "Ceapa galbena",
        brand: "Ceapa",
        price: 1.39,
        isPerUnit: false,
        expirationDate: stringToDate("29.11.2021"),
        isRefrigerated: false,
        image:
        "https://www.cora.ro/images/products/1793205/gallery/1793205_hd_1.jpg",
        warehouseId: 1),
    Product(
        id: 4,
        name: "Inghetata Topgun Over cu vanilie,260ml",
        brand: "Nestle",
        price: 5.15,
        isPerUnit: true,
        expirationDate: stringToDate("31.12.2021"),
        isRefrigerated: true,
        image:
        "https://www.auchan.ro/public/images/hb0/h48/h00/inghetata-topgun-over-cu-vanilie-260ml-9434379255838.jpg",
        warehouseId: 1)
  ];

  List<Product> getAllProducts(){
    return _products;
  }
  
  Product? saveProduct(Product product){
    _products.add(product);
    return null;
  }
  
  Product? updateProduct(Product product){
    Product oldProduct = _products.firstWhere((pr) => pr.id == product.id);
    _products.remove(oldProduct);
    _products.add(product);
    return oldProduct;
  }

  Product? deleteProduct(Product product){
    Product oldProduct = _products.firstWhere((pr) => pr.id == product.id);
    _products.remove(oldProduct);
    return oldProduct;
  }

  int getSize(){
    var products;
    return products.length;
  }
}