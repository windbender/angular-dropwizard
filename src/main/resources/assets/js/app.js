
var app = angular.module('dropangular', [ 'ngCookies', 'ngResource', 'http-auth-interceptor' ]);

app.config(function ($routeProvider) {
    $routeProvider
    .when('/books', {templateUrl: '/views/books/list.html', controller: 'BookListController'})
    .when('/books/:bookId', {templateUrl: '/views/books/detail.html', controller: 'BookDetailController'});
});

app.config(function(authServiceProvider) {
	authServiceProvider.addIgnoreUrlExpression(function(response) {
		// this keeps the auth provider from intercepting the actual login attempt!
		return response.config.url === "users/login";
	});
});

app.factory('Book', function ($resource) {
        var Book = $resource('/api/books/:bookId', {bookId: '@id'},
            {update: {method: 'PUT'}});
        Book.prototype.isNew = function(){
            return (typeof(this.id) === 'undefined');
        }
        return Book;
    }
);

app.factory('User', function($resource) {
	var User = $resource('/api/users/:id',
			{}, {
				'query':  {method:'GET', isArray:true}
			});
	return User;
});


app.directive('authDemoApplication', function() {
	return {
		restrict : 'C',
		link : function(scope, elem, attrs) {
			// once Angular is started, remove class:
			elem.removeClass('waiting-for-angular');

			var login = elem.find('#login-holder');
			var main = elem.find('#content');

			login.hide();

			scope.$on('event:auth-loginRequired', function() {
				login.slideDown('slow', function() {
					login.show();
					main.hide();
				});
			});
			scope.$on('event:auth-loginConfirmed', function() {
				main.show();
				login.slideUp();
				login.hide();
			});
			
		}
	};
});

app.factory('CurUser',function($http) {
	var CurUser = {};
	var myCurUser = {
        username:"(none)"
    };
    
    $http.get('api/users/getLoggedIn').success(function(data) {
    	myCurUser = data;
    });
	
    CurUser.getCurUser = function() {
    	return myCurUser;
    }
    CurUser.setUsername = function(inp) {
    	myCurUser.username = inp;
    }
	return CurUser;

});

app.factory('Menu', function($resource) {
	var Menu = $resource('/api/users/menus',
			{}, {
				'query':  {method:'GET', isArray:true}
			});
	return Menu;
});

app.controller({
	MenuController : function($scope, Menu) {
		$scope.menus = Menu.query();
		
		$scope.$on('reloadMenus', function() {
			$scope.menus = Menu.query();
		});
		
	}

});

app.controller({
	LogoutController : function($rootScope, $scope,$http, $window, CurUser) {
		$scope.logout = function() {
			$http.post('api/users/logout').success(function() {
				CurUser.setUsername(undefined);
				$window.location.href = "/#/";
				$rootScope.$broadcast('reloadMenus');
			});
		};
		$scope.gotoLogin = function() {
			$rootScope.$broadcast('event:auth-loginRequired');
		};
		$scope.isLoggedIn = function() {
			if(CurUser.getCurUser().username == "(none)") return false;
			if(CurUser.getCurUser().username == null) return false;
			if(CurUser.getCurUser().username === undefined) return false;
            return true;
		};
		
		$scope.curUser = CurUser;
	}
})

app.run(function($rootScope) {
	$rootScope.$broadcast('checkLoggedIn');
});

app.controller({
	LoginController : function($cookies, $scope, $rootScope, $http, authService, CurUser) {
		$scope.curUser = CurUser;
		
		$scope.submit = function() {
			$scope.failMsg = "";
			$http.post('api/users/login', {
				username : $scope.username,
				password : $scope.password
			}).success(function() {
				authService.loginConfirmed();
				$scope.curUser.setUsername($scope.username);
				$scope.failMsg = "";
				$rootScope.$broadcast('reloadMenus');
				$scope.password = "";
			}).error(function(data, status, headers, config) {
				$scope.failMsg = "sorry that user or password invalid";
				$scope.password = "";
			});
		};

	}

});

app.controller({
	BookListController: function($scope, Book) {
	    $scope.books = Book.query();

	    $scope.deleteBook = function(book) {
	        book.$delete(function() {
	            $scope.books.splice($scope.books.indexOf(book),1);
	            toastr.success("Deleted");
	        });
	    }
	}
});

app.controller({
	BookDetailController: function($scope, $routeParams, $location, Book) {
	    var bookId = $routeParams.bookId;
	    
	    if (bookId === 'new') {
	        $scope.book = new Book();
	        $scope.showSave = true;
	    } else {
	        $scope.book = Book.get({bookId: bookId});
	        $scope.showSave = false;
	    }
	    
	    
	    
	    $scope.save = function () {
	        if ($scope.book.isNew()) {
	            $scope.book.$save(function (book, headers) {
	                toastr.success("Created");
	                var location = headers('Location');
	                var id = location.substring(location.lastIndexOf('/') + 1);
	                $location.path('/books/' + id);
	            });
	        } else {
	            $scope.book.$update(function() {
	                toastr.success("Updated");
	                $location.path('/books/');
	            });
	        }
	    };
	}

});


