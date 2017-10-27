angular.module('JWTDemoApp').config(function($stateProvider, $urlRouterProvider) {
	
	// the ui router will redirect if a invalid state has come.
	$urlRouterProvider.otherwise('/page-not-found');
	$urlRouterProvider.when("", "/login");

	// parent view - navigation state
	$stateProvider.state('nav', {
		abstract : true,
		url : '',
		views : {
			'nav@' : {
				templateUrl : 'app/angular/views/nav.html',
				controller : 'NavController'
			}
		}
	}).state('login', {
		parent : 'nav',
		url : '/login',
		views : {
			'content@' : {
				templateUrl : 'app/angular/views/login.html',
				controller : 'LoginController'
			}
		}
	}).state('users', {
		parent : 'nav',
		url : '/users',
		data : {
			role : 'ADMIN'
		},
		views : {
			'content@' : {
				templateUrl : 'app/angular/views/users.html',
				controller : 'UsersController',
			}
		}
	}).state('home', {
		parent : 'nav',
		url : '/',
		views : {
			'content@' : {
				templateUrl : 'app/angular/views/home.html',
				controller : 'HomeController'
			}
		}
	}).state('page-not-found', {
		parent : 'nav',
		url : '/page-not-found',
		views : {
			'content@' : {
				templateUrl : 'app/angular/views/page-not-found.html',
				controller : 'PageNotFoundController'
			}
		}
	}).state('access-denied', {
		parent : 'nav',
		url : '/access-denied',
		views : {
			'content@' : {
				templateUrl : 'app/angular/views/access-denied.html',
				controller : 'AccessDeniedController'
			}
		}
	}).state('register', {
		parent : 'nav',
		url : '/register',
		views : {
			'content@' : {
				templateUrl : 'app/angular/views/register.html',
				controller : 'RegisterController'
			}
		}
	});
});
