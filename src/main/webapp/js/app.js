'use strict';

// --
// Routing !
// --
angular.module('tp4', ['tp4.services', 'tp4.controllers'])
.config(['$routeProvider', function($routeProvider) {

    // Login page
    $routeProvider.when('/login',
    {
      templateUrl:  'pages/login.html',
      controller:   'LoginController'
    });

    // Index page
    $routeProvider.when('/index',
    {
      templateUrl:   'pages/index.html',
      controller:    'IndexController'
    });

    // Players
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

    // Team
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

    // Matchs
    $routeProvider.when('/match-list',
    {
      templateUrl:   'pages/matchs/list.html',
      controller:    'MatchListController'
    });
    $routeProvider.when('/match-create',
    {
      templateUrl:   'pages/matchs/create.html',
      controller:    'MatchCreateController'
    });
    $routeProvider.when('/match-score/:id',
    {
      templateUrl:   'pages/matchs/score.html',
      controller:    'MatchController'
    });
    $routeProvider.when('/match-official',
    {
      templateUrl:   'pages/matchs/officials.html',
      controller:    'MatchController'
    });

    // Officials

    /*$routeProvider.otherwise(
    {
      redirectTo:   '/login'
    });*/
}]);
