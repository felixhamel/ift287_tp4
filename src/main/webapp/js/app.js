'use strict';

// --
// Routing !
// --
var app = angular.module('tp4', ['tp4.services', 'tp4.controllers']);

app.config(['$routeProvider', function($routeProvider) {

  // Login page
  $routeProvider.when('/login', {
    templateUrl:  'pages/login.html',
    controller:   'LoginController'
  });
  $routeProvider.when('/disconnect',{
    templateUrl:  'pages/disconnect.html',
    controller:    'LoginController'
  });

  // Index page
  $routeProvider.when('/index', {
    templateUrl:   'pages/index.html'
  });

  // Players
  $routeProvider.when('/player-list', {
    templateUrl:  'pages/players/list.html',
    controller:   'PlayerListController'
  });
  $routeProvider.when('/player-view/:id', {
    templateUrl:  'pages/players/view.html',
    controller:   'PlayerViewController'
  });
  $routeProvider.when('/player-create', {
    templateUrl:  'pages/players/create.html',
    controller:   'PlayerCreateController'
  });

  // Team
  $routeProvider.when('/team-list', {
    templateUrl:  'pages/teams/list.html',
    controller:   'TeamListController'
  });
  $routeProvider.when('/team-create', {
    templateUrl:  'pages/teams/create.html',
    controller:   'TeamCreateController'
  });
  $routeProvider.when('/team-view/:id',{
    templateUrl:  'pages/teams/view.html',
    controller:   'TeamViewController'
   });
   $routeProvider.when('/team-import', {
     templateUrl:  'pages/teams/import.html'
   });

  // Matchs
  $routeProvider.when('/match-list', {
    templateUrl:   'pages/matchs/list.html',
    controller:    'MatchListController'
  });
  $routeProvider.when('/match-create', {
    templateUrl:   'pages/matchs/create.html',
    controller:    'MatchCreateController'
  });
  $routeProvider.when('/match-score/:id', {
    templateUrl:   'pages/matchs/score.html',
    controller:    'MatchController'
  });
  $routeProvider.when('/match-official', {
    templateUrl:   'pages/matchs/officials.html',
    controller:    'MatchController'
  });

  // Officials
  $routeProvider.when('/official-list', {
  	templateUrl:  'pages/officials/list.html',
  	controller:   'OfficialListController'
  });
  $routeProvider.when('/official-arbitrate', {
  	templateUrl:  'pages/officials/arbitrate.html',
  	controller:   'OfficialArbitrateController'
  });
  $routeProvider.when('/official-create', {
  	templateUrl:  'pages/officials/create.html',
  	controller:   'OfficialCreateController'
  });

  $routeProvider.otherwise({
    redirectTo:   '/login'
  });
}]);

app.run(['$rootScope', '$location', '$http', function($rootScope, $location, $http) {
    $rootScope.$on('$routeChangeSuccess', function () {
        $http.get('/rest/settings/connection').success(function(data, status, headers, config) {
          if(data[0] == 'f') {
            var notConnected = true;
            $location.path('/login');
          }
        });
    });
}]);
