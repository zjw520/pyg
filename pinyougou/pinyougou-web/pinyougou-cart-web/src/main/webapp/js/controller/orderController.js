/** 定义订单控制器 */
app.controller("orderController", function ($scope, $controller, $interval, $location, baseService) {
    /** 指定继承cartController */
    $controller("cartController", {$scope: $scope});
    /** 根据登录用户获取地址 */
    $scope.findAddressByUser = function () {
        baseService.sendGet("/order/findAddressByUser")
            .then(function (response) {
                $scope.addressList = response.data;
                for (var i in response.data) {
                    if (response.data[i].isDefault == 1) {
                        $scope.address = response.data[i];
                        break;
                    }
                }
            });
    };


    $scope.isSelectedAddress = function (item) {
        return $scope.address == item;
    }

    $scope.selectAddress = function (item) {
        $scope.address = item;
    }

    $scope.order = {paymentType: '1'}

    $scope.selectPayType = function (type) {
        $scope.order.paymentType = type;
    }

    $scope.saveOrder = function () {
        $scope.order.receiverAreaName = $scope.address.address;
        $scope.order.receiverMobile = $scope.address.mobile;
        $scope.order.receiver = $scope.address.contact;

        baseService.sendPost("/order/save", $scope.order).then(function (response) {
            if (response.data) {
                if ($scope.order.paymentType == 1) {
                    location.href = "/order/pay.html";
                } else {
                    location.href = "order/paysuccess.html";
                }
            } else {
                alert("提交订单失败")
            }
        })

    }

    $scope.genPayCode = function () {
        baseService.sendGet("/order/genPayCode").then(function (response) {
            $scope.money = (response.data.totalFee / 100).toFixed(2);
            $scope.outTradeNo = response.data.outTradeNo;
            var qr = new QRious({
                element: document.getElementById('qrious'),
                size: 250,
                level: 'H',
                value: response.data.codeUrl
            })

            var timer = $interval(function () {
                baseService.sendGet("/order/queryPayStatus?outTradeNo=" + $scope.outTradeNo).then(function (response) {
                    if (response.data.status == 1) {
                        $interval.cancel(timer);
                        location.href = "/order/paysuccess.html?money=" + $scope.money;
                    }
                    if (response.data.status == 3) {
                        $interval.cancel(timer);
                        location.href = "/order/payfail.html"
                    }
                })
            }, 3000, 60);

            timer.then(function () {
                alert("微信支付二维码失效!")
            })
        })
    }

    $scope.getMoney = function () {
        return $location.search().money;
    }


})


;
