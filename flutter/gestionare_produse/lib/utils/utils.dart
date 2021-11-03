import 'package:flutter/cupertino.dart';
import 'package:intl/intl.dart';

String dateToString(DateTime date){
  DateFormat formatter = DateFormat("dd.MM.yyyy");
  return formatter.format(date);
}

DateTime stringToDate(String date){
  DateFormat formatter = DateFormat("dd.MM.yyyy");
  return formatter.parse(date);
}

Color backgroundColorByExpirationDate(DateTime date) {
  if (date.isBefore(DateTime.now()) || date == DateTime.now())
    return Color(0xFFFFA8A8);
  else {
    int diff = date
        .difference(DateTime.now())
        .inDays;
    if (diff <= 7) {
      return Color(0xFFFFB377);
    } else {
      return Color(0xFFFFFFFF);
    }
  }
}