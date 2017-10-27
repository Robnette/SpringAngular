Vue.component('navbar-component', {
    props: ['user'],
    template: '#navbarComponent',
    methods:{
        testClick: function(user){
            console.log("user", user)
            this.$emit("emit-test");
        },
        logout: function(){
            this.$http.post('logout');
            app.user = null;
            delete Vue.http.headers.common.Authorization
            localStorage.removeItem('tokenAndUser');
            this.$router.replace("/login");
        }
    }
})

var Page404 = {
    template: '<div>404, Page not found</div>'
};

var LoginView = {
    name: "loginView",
    template: "#loginView",
    data: function() {
        return {
            message: null,
            username: null,
            password: null
        };
    },
    methods:{
        sumbitForm: function(){
            console.log("username", this.username, "pwd", this.password);

            this.$http.post(
                'authenticate',
                {username: this.username, password: this.password}
            ).then(function(res) {
                data = res.body;
                if (data.token) {
                    this.message = "";
                    Vue.http.headers.common['Authorization'] = 'Bearer ' + data.token;
                    toStorage = {
                        token: data.token,
                        user: data.user
                    };
                    localStorage.setItem('tokenAndUser', JSON.stringify(toStorage));
                    app.user = data.user;
                    this.$router.replace("/");
                }
                console.log(res);
            }, function(error){
                this.message = "Authentication Failed !";
            })
        }
    },
    beforeMount: function(){
        console.log("LoginView beforeMount");
    },
    mounted: function(){
        console.log("LoginView mounted");
    }
};

var HomeView = {
    name: "homeView",
    template: "#homeView",
    props: ['user'],
    beforeMount: function(){
        console.log("HomeView beforeMount");
    },
    mounted: function(){
        console.log("HomeView mounted");
    }
};

var RegisterView = {
    name: "registerView",
    template: "#registerView",
    data: function() {
        return {
            appUser: {
                name: null,
                username: null,
                password: null
            },
            confirmPassword: null,
            message: null
        };
    },
    computed:{
        isValid: function(){
            return this.appUser.name != null && this.appUser.name != ""
                && this.appUser.username != null && this.appUser.username != ""
                && this.appUser.password != null && this.appUser.password != ""
                && this.appUser.password == this.confirmPassword;
        }
    },
    methods:{
        isValidField: function(data){
            return data == null || data != null && data != "";
        },
        sumbitForm: function(){
            this.$http.post(
                'register',
                this.appUser
            ).then(function(res) {
                this.appUser = {
                    name: null,
                    username: null,
                    password: null
                };
            	this.confirmPassword = null;
                this.message = "Registration successfull !";
            }).catch(function(error) {
                this.message = error.data.message;
            });
        }
    }
}
// Create the router
var router = new VueRouter({
    root: '',
//	base: window.location.href,
	routes: [
		{
		    path: '/',
		    component: HomeView,
            beforeEnter: function (to, from, next) {
                console.log("router HomeView beforeEnter");
                next();
            }

        },
		{
		    path: '/login',
		    component: LoginView,
            beforeEnter: function (to, from, next) {
                console.log("router LoginView beforeEnter");
                if(Vue.http.headers.common.Authorization){
                    next("/");
                }else{
                    next();
                }
            }
        },
		{
		    path: '/register',
		    component: RegisterView,
            beforeEnter: function (to, from, next) {
                console.log("router RegisterView beforeEnter");
                if(Vue.http.headers.common.Authorization){
                    next("/");
                }else{
                    next();
                }
            }
        },
        {
            path: '*',
            component: Page404
        }
	]
});


var app = new Vue({
	router,
	el: '#vueApp',
	data: {
	    user: null
	},
	methods: {
	    consoleLog: function(){
	        console.log("test ?");
	    }
	},
	beforeCreate: function(){
	    console.log("beforeCreate");
	},
	created: function(){
	    console.log("created");
	    var tokenAndUser = JSON.parse(localStorage.getItem('tokenAndUser'));
        if(tokenAndUser == null){
            this.$router.replace("/login");
        }else{
            Vue.http.headers.common['Authorization'] = 'Bearer ' + tokenAndUser.token;
            this.user = tokenAndUser.user;
        }
	}
});