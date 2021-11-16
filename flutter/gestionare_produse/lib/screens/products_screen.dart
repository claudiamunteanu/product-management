import 'package:flutter/material.dart';
import 'package:gestionare_produse/repository/products_repository.dart';
import 'package:gestionare_produse/screens/new_product.dart';
import 'package:gestionare_produse/utils/utils.dart';
import 'package:gestionare_produse/view_models/new_product_view_model.dart';
import 'package:gestionare_produse/view_models/products_view_model.dart';
import 'package:provider/provider.dart';
import '../domain/product.dart';

class ProductsScreen extends StatefulWidget {
  ProductRepository _repo;

  ProductsScreen(this._repo);

  @override
  _ProductsScreenState createState() => _ProductsScreenState(_repo);
}

class _ProductsScreenState extends State<ProductsScreen> {

  List<Product> _products = [];
  late ProductsViewModel _viewModel;


  _ProductsScreenState(ProductRepository repo){
    _viewModel = ProductsViewModel(repo);
  }

  @override
  void initState() {
    super.initState();
    // context.select<ServiceClass, bool>((service)=>service.inGeoFence)
    _products = _viewModel.getAllProducts();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.grey[200],
        appBar: AppBar(
          title: Text('Aplicatie Gestiune'),
          centerTitle: true,
        ),
        body: ListView.builder(
              itemCount: _products.length,
              itemBuilder: (context, index) {
                return ProductCard(
                    product: _products[index],
                    view: () async {
                      Product product = _products[index];
                      final value = await Navigator.pushNamed(context, '/details', arguments: {
                        'id' : product.id,
                        'name' : product.name,
                        'brand' : product.brand,
                        'price': product.price,
                        'isPerUnit' : product.isPerUnit,
                        'expDate': product.expirationDate,
                        'isRefrigerated' : product.isRefrigerated,
                        'image' : product.image,
                        'warehouseId' : product.warehouseId
                      });
                      setState(() {
                        _products = _viewModel.getAllProducts();
                      });
                    });
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          final value = await Navigator.pushNamed(context, '/newProduct');
          // setState(() {
          //   _products = _viewModel.getAllProducts();
          // });
        },
        child: Icon(Icons.add),
      ),
    );
  }
}

class ProductCard extends StatelessWidget {
  final Product product;
  final Function() view;

  ProductCard({required this.product, required this.view});

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: view,
      child: Container(
        height: 120.0,
        child: Card(
          margin: EdgeInsets.fromLTRB(8.0, 8.0, 8.0, 0.0),
          color: backgroundColorByExpirationDate(product.expirationDate),
          elevation: 8.0,
          child: Padding(
            padding: EdgeInsets.all(8.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  flex: 1,
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(0.0, 0.0, 8.0, 0.0),
                    child: Image.network(
                      product.image,
                      height: 100.0,
                      width: 80.0,
                    ),
                  ),
                ),
                Expanded(
                    flex: 2,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          product.name,
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(
                            fontSize: 16.0,
                          ),
                          maxLines: 1,
                        ),
                        Padding(
                          padding: const EdgeInsets.symmetric(
                              vertical: 8.0, horizontal: 0.0),
                          child: Text(
                            product.brand,
                            overflow: TextOverflow.ellipsis,
                            style: TextStyle(
                              fontSize: 14.0,
                            ),
                            maxLines: 1,
                          ),
                        ),
                        Text(
                          'Exp: ${dateToString(product.expirationDate)}',
                          style: TextStyle(
                            fontSize: 14.0,
                          ),
                        ),
                      ],
                    )),
                Expanded(
                    flex: 1,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Padding(
                          padding: const EdgeInsets.fromLTRB(0.0, 0.0, 0.0, 4.0),
                          child: Text(
                            product.isPerUnit
                                ? product.price.toString() + " Lei"
                                : product.price.toString() + " Lei\n/kg",
                            style: TextStyle(
                              fontSize: 20.0,
                            ),
                            textAlign: TextAlign.end,
                          ),
                        ),
                        Container(
                            child: (product.isRefrigerated)
                                ? Padding(
                              padding: const EdgeInsets.fromLTRB(
                                  0.0, 4.0, 0.0, 0.0),
                              child: Icon(
                                Icons.ac_unit,
                                color: Color(0xFF3781FF),
                              ),
                            )
                                : Container())
                      ],
                    ))
              ],
            ),
          ),
        ),
      ),
    );
  }
}
