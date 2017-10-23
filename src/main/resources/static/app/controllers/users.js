angular.module('JWTDemoApp')
// Creating the Angular Controller
.controller('UsersController', function($http, $scope, AuthService) {
	var edit = false;
	$scope.buttonText = 'Create';
	var init = function() {
		$http.get('api/users').then(function(res) {

			$scope.users = res.data;
			
			$scope.userForm.$setPristine();
			$scope.message='';
			$scope.appUser = null;
			$scope.buttonText = 'Create';
			
		}).catch(function(error) {
			$scope.message = error.data.message;
			AuthService.checkError(error);
		});
	};
	$scope.initEdit = function(appUser) {
		edit = true;
		$scope.appUser = appUser;
		$scope.message='';
		$scope.buttonText = 'Update';
	};
	$scope.initAddUser = function() {
		edit = false;
		$scope.appUser = null;
		$scope.userForm.$setPristine();
		$scope.message='';
		$scope.buttonText = 'Create';
	};
	$scope.deleteUser = function(appUser) {
		$http.delete('api/users/'+appUser.uid).then(function(res) {
			$scope.deleteMessage ="Success!";
			init();
		}).catch(function(error) {
			$scope.deleteMessage = error.data.message;
		});
	};
	var editUser = function(){
		$http.put('api/users', $scope.appUser).then(function(res) {
			$scope.appUser = null;
//			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "Editting Success";
			init();
		}).catch(function(error) {
			$scope.message =error.data.message;
		});
	};
	var addUser = function(){
		$http.post('api/users', $scope.appUser).then(function(res) {
			$scope.appUser = null;
//			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "User Created";
			init();
		}).catch(function(error) {
			$scope.message = error.data.message;
		});
	};
	$scope.submit = function() {
		if(edit){
			editUser();
		}else{
			addUser();	
		}
	};
	init();

});
