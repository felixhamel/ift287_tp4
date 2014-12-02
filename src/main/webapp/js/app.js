'use strict';

// --
// Routing !
// --
angular.module('tp4', ['tp4.services', 'tp4.controllers']).
  config(['$routeProvider', function($routeProvider) {
      $routeProvider.when('/login',
      {
        templateUrl:  'pages/login.html',
        controller:   'LoginController'
      });
      $routeProvider.when('/player-list',
      {
        templateUrl:  'pages/players/list.html',
        controller:   'PlayerListController'
      });
      $routeProvider.when('/player-view/:id',
      {
        templateUrl:  'pages/players/view.html',
        controller:   'PlayerViewController'
      });
      $routeProvider.when('/player-create',
      {
        templateUrl:  'pages/players/create.html',
        controller:   'PlayerCreateController'
      });
      $routeProvider.when('/team-list',
      {
        templateUrl:  'pages/teams/list.html',
        controller:   'TeamListController'
      });
      $routeProvider.when('/team-create',
      {
        templateUrl:  'pages/teams/create.html',
        controller:   'TeamCreateController'
      });
      /*$routeProvider.otherwise(
      {
        redirectTo:   '/login'
      });*/
  }]);

// Declare app level module which depends on filters, and services
/*
angular.module('ngdemo', ['ngdemo.filters', 'ngdemo.services', 'ngdemo.directives', 'ngdemo.controllers']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/dummy', {templateUrl: 'partials/dummy.html', controller: 'DummyCtrl'});
        $routeProvider.when('/user-list', {templateUrl: 'partials/user-list.html', controller: 'UserListCtrl'});
        $routeProvider.when('/user-detail/:id', {templateUrl: 'partials/user-detail.html', controller: 'UserDetailCtrl'});
        $routeProvider.when('/user-creation', {templateUrl: 'partials/user-creation.html', controller: 'UserCreationCtrl'});
        $routeProvider.otherwise({redirectTo: '/dummy'});
    }]);
*/
