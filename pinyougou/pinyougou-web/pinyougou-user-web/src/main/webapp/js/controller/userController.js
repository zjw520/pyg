/** 定义控制器层 */
app.controller('userController', function ($scope,$controller , baseService) {


    $scope.user = {};

    $scope.save = function () {
        if ($scope.user.password != $scope.password) {
            alert("密码不一致,请重新输入!");
            return;
        }
        baseService.sendPost("/user/save?smsCode="+$scope.smsCode, $scope.user).then(function (response) {
            if (response.data) {
                alert("注册成功!");
                $scope.user = {};
                $scope.password = "";
                $scope.smsCode = "";
            } else {
                alert("注册失败!");
            }
        })
    }

    $scope.sendCode = function () {
        if ($scope.user.phone) {
            baseService.sendGet("/user/sendCode?phone=" + $scope.user.phone).then(function (response) {
                alert(response.data ? "发送成功!" : "发送失败!");
            });
        } else {
            alert("请输入手机号码")
        }
    }

    $scope.showName = function(){
        baseService.sendGet("/user/showName").then(function (response) {
            // 定义重定向URL
            $scope.redirectUrl = window.encodeURIComponent(location.href);
            $scope.loginName = response.data.loginName;
        })
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    $scope.findUser = function () {
        baseService.sendGet("/user/findUser").then(function (response) {
            $scope.userData=response.data;
            $scope.userData.address = JSON.parse($scope.userData.address);
            $scope.occupationid = $scope.userData.constructor;

            var time = new Date($scope.userData.birthday);
            var y = time.getFullYear() ;
            var m = time.getMonth() + 1 < 9 ? "0" + (time.getMonth() + 1): time.getMonth() + 1;
            var d = time.getDate() < 9 ? "0" + time.getDate(): time.getDate();
            $scope.userData.birthday = y + "-" + m + "-" + d;
        })
    };




    $scope.findItemCatByParentId = function() {
        baseService.sendGet("/province/findProvinces").then(function (response) {
           $scope.provincesList = response.data;
        })
    };

    $scope.$watch("userData.address.provinceid", function (newVal) {
        if (newVal) {
            baseService.sendGet("/cityid/findCityIdbyProinceid?provinceid=" + newVal).then(function (value) {
                $scope.cityList=value.data;
            })
        } else {
            $scope.cityList = [];
        }
    });

    $scope.$watch("userData.address.cityid", function (newVal) {
        if (newVal) {
           baseService.sendGet("/areaid/findAreaidByCityid?cityid=" + newVal).then(function (value) {
               $scope.areaidList=value.data;
           })
        } else {
            $scope.areaidList = [];
        }
    })

    $scope.occupations=['程序员','产品经理','UI设计员']



    /**上传图片 */
    $scope.uploadFile = function(){
        baseService.uploadFile().then(function(response) {
            /** 如果上传成功，取出url */
            if(response.data.status == 200){
                /** 设置图片访问地址 */
                $scope.userData.headPic = response.data.url;
            }else{
                alert("上传失败！");
            }
        });
    };


    //保存添加用户
    $scope.addUser = function (userData){
        baseService.sendPost("/user/updateUser",userData).then(function (response) {
            if(response.data){
              alert("修改成功");
              $scope.userData = {};
              $scope.findUser();
            }else {
                alert("修改失败");
            }
        })
    }


    $scope.user = {}
    $scope.save = function () {
        if ($scope.user.password != $scope.password) {
            alert("密码不一致,请重新输入!");
            return;
        }
        baseService.sendPost("/user/save?smsCode="+$scope.smsCode, $scope.user).then(function (response) {
            if (response.data) {
                alert("注册成功!");
                $scope.user = {};
                $scope.password = "";
                $scope.smsCode = "";
            } else {
                alert("注册失败!");
            }
        })
    }

    $scope.sendCode = function () {
        if ($scope.user.phone) {
            baseService.sendGet("/user/sendCode?phone=" + $scope.user.phone).then(function (response) {
                alert(response.data ? "发送成功!" : "发送失败!");
            });
        } else {
            alert("请输入手机号码")
        }
    }

    $scope.showName = function(){
        baseService.sendGet("/user/showName").then(function (response) {

            $scope.redirectUrl = window.encodeURIComponent(location.href);
            $scope.loginName = response.data.loginName;
        })
    };


    $scope.findUser = function () {
        baseService.sendGet("/user/findUser").then(function (response) {
            $scope.userData=response.data;
        })
    }

    $scope.findAll = function () {
        baseService.sendGet("/address/findAll").then(function (response) {
            $scope.adressList = response.data.addressList;
            $scope.pList = response.data.provinceList;

        })

    }
    $scope.show = function (entity) {
        $scope.entity = JSON.parse(JSON.stringify(entity));

    }

    $scope.aliasArra=["家里","父母家","公司"]
    $scope.choseAlias = function (num) {
        $scope.entity.alias =$scope.aliasArra[num];

    }
    $scope.findprovinceByPid = function (id,list) {
        baseService.sendGet("/total/address","id="+id).then(function (response) {
            $scope[list]=response.data
        })
    }


    $scope.$watch("entity.province_id",function (newVal,oldVal) {

        if(newVal){
            $scope.findprovinceByPid(newVal,'citiesList')

        }else{
            $scope.citiesList=[];

        }

    })
    $scope.$watch("entity.city_id",function (newVal,oldVal) {
        if(newVal){
            $scope.findprovinceByPid(newVal,'townList')

        }else{
            $scope.townList=[];

        }

    })

    $scope.updataDefaluStatus = function (entity) {
        baseService.sendPost("/address/addressDefaultStatus",entity).then(function (response) {
            if (response.data){
                $scope.findAll();

            }

        })

    }

    $scope.saveOrUpdare = function (entity) {
        var url ="/saveAddress";
        if(entity.id){
            url="/updateAddress";
        }
        baseService.sendPost("/address"+url,entity).then(function (reponse) {
            if(reponse.data){

                alert("保存成功")
                $scope.findAll();

            }else{
                alert("保存失败")
            }
        })
    }
    $scope.deleteAdressById = function (id) {
        baseService.sendGet("/address/delete","id="+id).then(function (response) {
            if (response.data) {
                alert("删除成功");
                $scope.findAll();

            }else{
                alert("删除失败");
            }
        })


    }


});