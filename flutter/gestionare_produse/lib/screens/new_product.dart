import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:gestionare_produse/domain/product.dart';
import 'package:gestionare_produse/repository/products_repository.dart';
import 'package:gestionare_produse/utils/utils.dart';
import 'package:gestionare_produse/view_models/new_product_view_model.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';
import 'package:provider/provider.dart';

class NewProduct extends StatefulWidget {
  ProductRepository _repo;

  NewProduct(this._repo);

  @override
  _NewProductState createState() => _NewProductState(_repo);
}

class _NewProductState extends State<NewProduct> {
  late NewProductViewModel _viewModel;

  _NewProductState(ProductRepository repo){
    _viewModel = NewProductViewModel(repo);
  }

  @override
  void initState() {
    super.initState();
  }

  final _formKey = GlobalKey<FormState>();

  final _nameController = TextEditingController();
  final _brandController = TextEditingController();
  final _priceController = TextEditingController();
  bool _perUnit = true;
  final _expDateController = TextEditingController();
  bool _isRefrigerated = false;
  final _imageController = TextEditingController();

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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Add new product'),
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
                                onChanged: (bool? value) {
                                  setState(() {
                                    _perUnit = value!;
                                  });
                                },
                              ),
                              const Text(
                                'per unit',
                                style: TextStyle(fontSize: 16.0),
                              ),
                              Radio(
                                value: false,
                                groupValue: _perUnit,
                                onChanged: (bool? value) {
                                  setState(() {
                                    _perUnit = value!;
                                  });
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
                                onChanged: (bool? value) {
                                  setState(() {
                                    _isRefrigerated = value!;
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
                                onPressed: () async {
                                  // Validate returns true if the form is valid, or false otherwise.
                                  if (_formKey.currentState!.validate()) {
                                    Product product = Product (
                                      id: _viewModel.getSize()+1,
                                      name : _nameController.value.text,
                                      brand: _brandController.value.text,
                                      price: double.parse(_priceController.value.text),
                                      isPerUnit: _perUnit,
                                      expirationDate: stringToDate(_expDateController.value.text),
                                      isRefrigerated: _isRefrigerated,
                                      image: _imageController.value.text,
                                      warehouseId: 1
                                    );
                                    Product? returnProduct = await Provider.of<NewProductViewModel>(context, listen: false)
                                    .saveProduct(product);
                                    if(returnProduct==null){
                                      ScaffoldMessenger.of(context).showSnackBar(
                                        const SnackBar(
                                            content: Text(
                                                'Product added successfully!')),
                                      );
                                    } else {
                                      ScaffoldMessenger.of(context).showSnackBar(
                                        const SnackBar(
                                            content: Text(
                                                'There was a problem in adding the product')),
                                      );
                                    }
                                    Navigator.pop(context);
                                  }
                                },
                                child: const Text('Save'),
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
