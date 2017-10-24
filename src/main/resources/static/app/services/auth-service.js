angular.module('JWTDemoApp')
// Creating the Angular Service for storing logged user details
.service('AuthService', function($state, $http) {
	return {
		user : null,
		disconnect: function(){
		    $http.post('logout');
		    this.user = null;
		    delete $http.defaults.headers.common.Authorization
            localStorage.removeItem('tokenAndUser');
		},
		checkError: function(error){
		    if(error.data.status == 401 && error.data.error == "Unauthorized"){
		        alert(error.data.message);
		        this.disconnect();
                $state.go("login", {}, {reload: true});
		    }
		}
	}
});
