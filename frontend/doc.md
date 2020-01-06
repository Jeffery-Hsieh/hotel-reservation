## show availble rooms
GET hotel_reservation/hotels
**Request**
```
{
    region: xx,
    check_in_date: xxx-xx-xx,
    check_out_date: xxx-xx-xx,
    singleRoomNum: xx,
    doubleRoomNum: xx,
    quadRoomNum: xx
}
```

**Response**
```
[
    {
        hotelID: xx,
        region: xx,
        address: xxx,
        hotelStar: xxx,

        !!!下面三個不用傳
        singleAvailNum: xx,
        doubleAvailNum: xx,
        quadAvailNum: xx
    },
]
```

## confirm booking
POST hotel_reservation/hotels/{ hotelID }
**Request**
```
{
    customerName: xxx,
    check_in_date: xxx-xx-xx,
    check_out_date: xxx-xx-xx,
    singleRoomNum: xx,
    doubleRoomNum: xx,
    quadRoomNum: xx
}
```

**Response**
_success_
```
{
    orderID: xxx,

    !!!這邊不用(hotelID: xxx,
    customerName: xxx,
    check_in_date: xxx-xx-xx,
    check_out_date: xxx-xx-xx,
    singleRoomNum: xx,
    doubleRoomNum: xx,
    quadRoomNum: xx,)

    isPaid: false,
    !!!缺totalPrice
}
```

_fail_
```
{
    orderID: "",
    hotelID: "",
    totalPrice: 0,
    singleRoomNum: 0,
    doubleRoomNum: 0,
    quadRoomNum: 0,
    isPaid: false
}
```

## search order
GET hotel_reservation/orders/{ orderID }
_success_
```
{
    orderID: xxx,
    hotelID: xxx,
    customerName: xxx,
    check_in_date: xxx-xx-xx,
    check_out_date: xxx-xx-xx,
    singleRoomNum: xx,
    doubleRoomNum: xx,
    quadRoomNum: xx,
    isPaid: false
}
```

_fail_
```
{
    orderID: "",
    hotelID: "",
    totalPrice: 0,
    singleRoomNum: 0,
    doubleRoomNum: 0,
    quadRoomNum: 0,
    isPaid: false
}
```

## modify order
PUT hotel_reservation/orders/{ orderID }
**Request**
```
{
    check_in_date: xxx-xx-xx,
    check_out_date: xxx-xx-xx,
    singleRoomNum: xx,
    doubleRoomNum: xx,
    quadRoomNum: xx,
}
```

**Response**
_success_
```
{
    status: "success"
}
```

_fail_
```
{
    status: "failure"
}
```

## pay money
PUT hotel_reservation/orders/{ orderID }
**Request**
```
{
    isPaid: true or false,
}
```

**Response**
_success_
```
{
    status: "success"
}
```

_fail_
```
{
    status: "failure"
}
```

## cancel order
DELETE hotel_reservation/orders/{ orderID }
**Response**
_success_
```
{
    status: "success"
}
```

_fail_
```
{
    status: "failure"
}
```

## view comment
GET hotel_reservation/hotels/{ hotelID }/comments
**Response**
_success_
```
[
    {
        description: xxxxxxxxxx,
        starRate: x,
    },
]
```

_fail_
```
{
    status: "failure"
}
```

## add comment
POST hotel_reservation/hotels/{ hotelID }/comments
**Request**
```
{
    orderID: xxx,
    description: xxxxxxxxxxxxxxx,
    !!!!要加這個
    starRate: xxx,
}
```

**Response**
_success_
```
{
    status: "success"
}
```

_fail_
```
{
    status: "failure"
}
```