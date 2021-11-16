import 'package:flutter/material.dart';
import 'package:gestionare_produse/domain/product.dart';
import 'package:gestionare_produse/repository/products_repository.dart';
import 'package:gestionare_produse/utils/utils.dart';
import 'package:gestionare_produse/view_models/new_product_view_model.dart';
import 'package:gestionare_produse/view_models/product_details_view_model.dart';

class ProductDetails extends StatefulWidget {
  ProductRepository _repo;

  ProductDetails(this._repo);

  @override
  _ProductDetailsState createState() => _ProductDetailsState(_repo);
}

class _ProductDetailsState extends State<ProductDetails> {
  late ProductDetailsViewModel _viewModel;

  _ProductDetailsState(ProductRepository repo) {
    _viewModel = ProductDetailsViewModel(repo);
  }

  Map product = {};

  final _formKey = GlobalKey<FormState>();

  int _id = 0;
  final _nameController = TextEditingController();
  final _brandController = TextEditingController();
  final _priceController = TextEditingController();
  bool _perUnit = true;
  final _expDateController = TextEditingController();
  bool _isRefrigerated = false;
  final _imageController = TextEditingController();
  int _warehouseId = 0;

  _selectDate(BuildContext context) async {
    final DateTime? selected = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2010),
      lastDate: DateTime(2100),
    );
    if (selected != null) {
      setState(() {
        _expDateController.value = TextEditingValue(
          text: dateToString(selected),
          selection: TextSelection.fromPosition(
            TextPosition(offset: dateToString(selected).length),
          ),
        );
      });
    }
  }

  _getImage() {
    if (_imageController.value.text.isNotEmpty) {
      return NetworkImage(_imageController.value.text);
    } else {
      return AssetImage('assets/placeholder_image.png');
    }
  }

  _getData() {
    product = ModalRoute.of(context)!.settings.arguments as Map;
    _id = product['id'];
    _nameController.value = TextEditingValue(text: product['name']);
    _brandController.value = TextEditingValue(text: product['brand']);
    _priceController.value =
        TextEditingValue(text: product['price'].toString());
    _perUnit = product['isPerUnit'];
    _expDateController.value =
        TextEditingValue(text: dateToString(product['expDate']));
    _isRefrigerated = product['isRefrigerated'];
    _imageController.value = TextEditingValue(text: product['image']);
    _warehouseId = product['warehouseId'];
  }

  Future<void> _showMyDialog() async {
    return showDialog<void>(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Are you sure?'),
          content: Text('Are you sure you want to delete this product?'),
          actions: [
            TextButton(
              child: const Text('NO'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: const Text('YES'),
              onPressed: () {
                Product product = Product(
                    id: _id,
                    name: _nameController.value.text,
                    brand: _brandController.value.text,
                    price: double.parse(_priceController.value.text),
                    isPerUnit: _perUnit,
                    expirationDate: stringToDate(_expDateController.value.text),
                    isRefrigerated: _isRefrigerated,
                    image: _imageController.value.text,
                    warehouseId: _warehouseId);
                Product? returnProduct = _viewModel.deleteProduct(product);
                if(returnProduct!=null){
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                        content: Text(
                            'Product deleted successfully!')),
                  );
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                        content: Text(
                            'There was a problem in deleting the product!')),
                  );
                }
                Navigator.pop(context);
                Navigator.pop(context);
              },
            ),
          ],
        );
      },
    );
  }

  @override
  void initState() {
    super.initState();
    Future.delayed(Duration.zero, () {
      _getData();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Product details'),
        centerTitle: true,
        backgroundColor: Colors.blue[700],
      ),
      body: Container(
        child: Center(
          child: Container(
            child: SingleChildScrollView(
              child: Column(
                children: [
                  Form(
                    key: _formKey,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 50.0, vertical: 20.0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.spaceAround,
                        children: [
                          TextFormField(
                            controller: _nameController,
                            decoration: const InputDecoration(
                              border: OutlineInputBorder(),
                              labelText: 'Name',
                              helperText: 'Required',
                            ),
                            validator: (name) {
                              return _viewModel.validateName(name);
                            },
                            keyboardType: TextInputType.text,
                          ),
                          SizedBox(height: 16.0),
                          TextFormField(
                            controller: _brandController,
                            decoration: const InputDecoration(
                              border: OutlineInputBorder(),
                              labelText: 'Brand',
                              helperText: 'Required',
                            ),
                            validator: (brand) {
                              return _viewModel.validateBrand(brand);
                            },
                            keyboardType: TextInputType.text,
                          ),
                          SizedBox(height: 16.0),
                          TextFormField(
                            controller: _priceController,
                            decoration: const InputDecoration(
                              border: OutlineInputBorder(),
                              labelText: 'Price',
                              helperText: 'Required',
                            ),
                            validator: (price) {
                              return _viewModel.validatePrice(price);
                            },
                            keyboardType: TextInputType.number,
                          ),
                          SizedBox(height: 4.0),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            children: [
                              Radio(
                                value: true,
                                groupValue: _perUnit,
                                onChanged: (bool? newValue) {
                                  setState(() {
                                    _perUnit = newValue!;
                                  });
                                  print(_perUnit);
                                },
                              ),
                              const Text(
                                'per unit',
                                style: TextStyle(fontSize: 16.0),
                              ),
                              Radio(
                                value: false,
                                groupValue: _perUnit,
                                onChanged: (bool? newValue) {
                                  setState(() {
                                    _perUnit = newValue!;
                                  });
                                  print(_perUnit);
                                },
                              ),
                              const Text(
                                'per Kg',
                                style: TextStyle(fontSize: 16.0),
                              ),
                            ],
                          ),
                          SizedBox(height: 4.0),
                          TextFormField(
                            focusNode: AlwaysDisabledFocusNode(),
                            controller: _expDateController,
                            decoration: const InputDecoration(
                              labelText: 'Expiration Date',
                              suffixIcon: Icon(Icons.date_range),
                              border: OutlineInputBorder(),
                              helperText: 'Required',
                            ),
                            onTap: () {
                              _selectDate(context);
                            },
                            validator: (expDate) {
                              return _viewModel.validateExpirationDate(expDate);
                            },
                          ),
                          SizedBox(height: 4.0),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            children: [
                              Checkbox(
                                value: _isRefrigerated,
                                onChanged: (bool? newValue) {
                                  setState(() {
                                    _isRefrigerated = newValue!;
                                  });
                                  print(_isRefrigerated);
                                },
                              ),
                              const Text(
                                'is refrigerable',
                                style: TextStyle(fontSize: 16.0),
                              ),
                            ],
                          ),
                          SizedBox(height: 4.0),
                          TextFormField(
                            controller: _imageController,
                            decoration: const InputDecoration(
                              border: OutlineInputBorder(),
                              labelText: 'Image URL',
                            ),
                            keyboardType: TextInputType.url,
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Image(
                              image: _getImage(),
                              height: 200.0,
                              width: 200.0,
                            ),
                          ),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              ElevatedButton(
                                onPressed: () {
                                  Navigator.pop(context);
                                },
                                child: const Text('Cancel'),
                              ),
                              ElevatedButton(
                                onPressed: () {
                                  if (_formKey.currentState!.validate()) {
                                    Product product = Product(
                                        id: _id,
                                        name: _nameController.value.text,
                                        brand: _brandController.value.text,
                                        price: double.parse(
                                            _priceController.value.text),
                                        isPerUnit: _perUnit,
                                        expirationDate: stringToDate(
                                            _expDateController.value.text),
                                        isRefrigerated: _isRefrigerated,
                                        image: _imageController.value.text,
                                        warehouseId: _warehouseId);
                                    Product? returnProduct = _viewModel.updateProduct(product);
                                    if(returnProduct!=null){
                                      ScaffoldMessenger.of(context).showSnackBar(
                                        const SnackBar(
                                            content: Text(
                                                'Product updated successfully!')),
                                      );
                                    } else {
                                      ScaffoldMessenger.of(context).showSnackBar(
                                        const SnackBar(
                                            content: Text(
                                                'There was a problem in updating the product!')),
                                      );
                                    }
                                    Navigator.pop(context);
                                  }
                                },
                                child: const Text('Update'),
                              ),
                              ElevatedButton(
                                onPressed: () {
                                  _showMyDialog();
                                },
                                child: const Text('Delete'),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class AlwaysDisabledFocusNode extends FocusNode {
  @override
  bool get hasFocus => false;
}
