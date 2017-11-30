angular.module('JWTDemoApp')
// Creating the Angular Controller
.controller('NavController', function($http, $scope, AuthService, $state, $rootScope) {
    $scope.user = AuthService.user;
    $scope.$on('LoginSuccessful', function() {
        $scope.user = AuthService.user;
    });
    $scope.$on('LogoutSuccessful', function() {
        $scope.user = null;
    });
    $scope.logout = function() {
        AuthService.disconnect();
        $rootScope.$broadcast('LogoutSuccessful');
        $state.go('login');
    };
});
