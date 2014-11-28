'use strict';

/* Controllers */

var app = angular.module('tp4.controllers', []);


// Clear browser cache (in development mode)
//
// http://stackoverflow.com/questions/14718826/angularjs-disable-partial-caching-on-dev-machine
app.run(function ($rootScope, $templateCache) {
    $rootScope.$on('$viewContentLoaded', function () {
        $templateCache.removeAll();
    });
});

// Player controller
app.controller(
  'PlayerListController',
  ['$scope', 'PlayersFactory', 'PlayerFactory', '$location', function($scope, PlayersFactory, PlayerFactory, $location) {

    // Create a new player
    $scope.createNewPlayer = function() {
      $location.path('/player-create/');
    }

    // Edit an existing user
    $scope.editPlayer = function(playerId) {
      $location.path('/player-view/' + playerId);
    };

    // Delete an existing user
    $scope.deletePlayer = function(playerId) {
      PlayerFactory.delete({ id: playerId });
      $scope.players = PlayersFactory.query();
    };

    // Retrieve all the players
    $scope.players = PlayersFactory.query();
  }
]);

// Player view controller
app.controller(
  'PlayerViewController',
  ['$scope', '$routeParams', 'PlayerFactory', '$location', function($scope, $routeParams, PlayerFactory, $location) {

    // Update player
    $scope.updatePlayer = function() {
      PlayerFactory.update($scope.player,
        function success() { $scope.updateWorked = true; },
        function error() { $scope.updateFailed = true; }
      );
      $location.path('/player-list/');
    };

    // Delete player
    $scope.deletePlayer = function() {
      PlayerFactory.delete({ id: $routeParams.id });
      $location.path('/player-list');
    };

    // Cancel view
    $scope.cancel = function() {
      $location.path('/player-list');
    };

    // Go get informations on the player
    $scope.player = PlayerFactory.query({ id: $routeParams.id });
  }
]);

// Player creation controller
app.controller(
  'PlayerCreateController',
  ['$scope', '$routeParams', 'PlayersFactory', '$location', function($scope, $routeParams, PlayersFactory, $location) {

    // Create user
    $scope.createNewPlayer = function() {
      PlayersFactory.create($scope.user).
      $location.path('/player-list');
    };

    // Cancel action
    $scope.cancel = function() {
      $location.path('/player-list');
    };
  }
]);

/*app.controller('DummyCtrl', ['$scope', 'DummyFactory', function ($scope, DummyFactory) {
    $scope.bla = 'bla from controller';
    DummyFactory.get({}, function (dummyFactory) {
        $scope.firstname = dummyFactory.firstName;
    })
}]);

app.controller('UserListCtrl', ['$scope', 'UsersFactory', 'UserFactory', '$location',
    function ($scope, UsersFactory, UserFactory, $location) {

        // callback for ng-click 'editUser':
        $scope.editUser = function (userId) {
            $location.path('/user-detail/' + userId);
        };

        // callback for ng-click 'deleteUser':
        $scope.deleteUser = function (userId) {
            UserFactory.delete({ id: userId });
            $scope.users = UsersFactory.query();
        };

        // callback for ng-click 'createUser':
        $scope.createNewUser = function () {
            $location.path('/user-creation');
        };

        $scope.users = UsersFactory.query();
    }]);

app.controller('UserDetailCtrl', ['$scope', '$routeParams', 'UserFactory', '$location',
    function ($scope, $routeParams, UserFactory, $location) {

        // callback for ng-click 'updateUser':
        $scope.updateUser = function () {
            UserFactory.update($scope.user);
            $location.path('/user-list');
        };

        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/user-list');
        };

        $scope.user = UserFactory.show({id: $routeParams.id});
    }]);

app.controller('UserCreationCtrl', ['$scope', 'UsersFactory', '$location',
    function ($scope, UsersFactory, $location) {

        // callback for ng-click 'createNewUser':
        $scope.createNewUser = function () {
            UsersFactory.create($scope.user);
            $location.path('/user-list');
        }
    }]);
*/
