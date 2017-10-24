angular.module('JWTDemoApp')
// Creating the Angular Controller
.controller('LoginController', function($http, $scope, $state, AuthService, $rootScope) {
	// method for login
	$scope.login = function() {
		// requesting the token by usename and passoword
		$http({
			url : 'authenticate',
			method : "POST",
			data : {
				username : $scope.username,
				password : $scope.password
			}
		}).then(function(res) {
		    data = res.data
			$scope.password = null;
			// checking if the token is available in the response
			if (data.token) {
				$scope.message = '';
				// setting the Authorization Bearer token with JWT token
				$http.defaults.headers.common['Authorization'] = 'Bearer ' + data.token;
				toStorage = {
				    token: data.token,
				    user: data.user
				};
				localStorage.setItem('tokenAndUser', angular.toJson(toStorage));

				// setting the user in AuthService
				AuthService.user = data.user;
				$rootScope.$broadcast('LoginSuccessful');
				// going to the home page
				$state.go('home');
			} else {
				// if the token is not present in the response then the
				// authentication was not successful. Setting the error message.
				$scope.message = 'Authetication Failed !';
			}
		}).catch(function(error) {
			// if authentication was not successful. Setting the error message.
			$scope.password = null;
			$scope.message = 'Authetication Failed !';
		});
	};
});
