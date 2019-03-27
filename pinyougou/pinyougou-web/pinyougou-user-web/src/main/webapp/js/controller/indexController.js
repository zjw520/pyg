/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){

    // 获取登录用户名
    $scope.showName = function () {
        baseService.sendGet("/user/showName").then(function (response) {
            // 获取响应数据
            $scope.loginName = response.data.loginName;
        });
    };

    //用户订单信息
    $scope.showOrder=function (){
        baseService.sendGet("/user/showOrder").then(function (response) {
            $scope.dataList=response.data;
        })
    };
   //付款状态: status : 1:未付款 2.已付款  3.已签收
    $scope.statusList=['待付款','买家已付款','已签收']
    //发货状态: 如果未付款,不显示.
    $scope.sendList=['','已发货','未发货']

});