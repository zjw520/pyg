/** 定义控制器层 */
app.controller('sellerController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/seller/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.seller.id) {
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/seller/" + url, $scope.seller)
            .then(function (response) {
                if (response.data) {
                    /** 重新加载数据 */
                    location.href = "/shoplogin.html";
                } else {
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/seller/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        /** 重新加载数据 */
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        } else {
            alert("请选择要删除的记录！");
        }
    };


    $scope.findSellerBySellerId=function () {
            baseService.sendGet("/seller/findSellerBySellerId").then(function (response) {
                $scope.sellerData=response.data;
            })
    }

    $scope.updateSeller=function (sellerData) {
        baseService.sendPost("/seller/updateSeller",sellerData).then(function (response) {
            if(response.data){
                alert("修改成功");
                $scope.findSellerBySellerId();
            }else{
                alert("修改失败");
            }
        })

    };

    $scope.password={pw:"",pw1:"",pw2:""};

    $scope.updatePasswrod=function (password) {

        if(password.pw1 == password.pw2){
            baseService.sendPost("/seller/updatePasswrod", password).then(function (response) {
                if(response.data){
                    alert("修改成功")
                }else {
                    alert("修改失败")
                }
            })
        }else {
            alert("两次输入的密码不正确");
        }
    }

});