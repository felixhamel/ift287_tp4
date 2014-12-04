'use strict';

/* Controllers */

var app = angular.module('tp4.controllers', []);


// Clear browser cache (in development mode)
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

    $scope.createPlayer = function() {
      $location.path('/player-create/');
    };

    $scope.editPlayer = function(playerId) {
      $location.path('/player-view/' + playerId);
    };

    $scope.deletePlayer = function(playerId) {
      PlayerFactory.delete({ id: playerId });
      $("#player-"+playerId).remove();
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
      $location.path('/player-view/' + player.id);
    };

    // Delete player
    $scope.deletePlayer = function() {
      PlayerFactory.delete({ id: $routeParams.id },
        function success() {
          $scope.deleteWorked = true;
          $location.path('/player-list');
        },
        function error() {
          $scope.deleteFailed = true;
          $location.path('/player-view');
        }
      );
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
  ['$scope', '$routeParams', 'PlayersFactory', 'TeamsFactory', 'TeamPlayerFactory', '$location', '$http',
  function($scope, $routeParams, PlayersFactory, TeamsFactory, TeamPlayerFactory, $location, $http) {

    // Create user
    $scope.createPlayer = function() {

      console.log("Team: ");
      console.log($scope.player);
      console.log("PlayerTeam: ");
      console.log($scope.playerTeam);

      // Create player
      $http.post('/rest/player', $scope.player).
        success(function(data, status, headers, config) {
          // Add player to team
          if($scope.playerTeam != null) {
            $scope.playerTeam.playerId = data.id; // Add player id
            $http.put('/rest/team/'+$scope.playerTeam.team.id+'/player', $scope.playerTeam).
            success(function(data, status, headers, config) {
              $location.path('/player-list');
            });
          } else {
            $location.path('/player-list');
          }
      });
    };

    // Cancel action
    $scope.cancel = function() {
      $location.path('/player-list');
    };

    // Retrieve all the teams
    $scope.teams = TeamsFactory.query();
  }
]);

// Teams controller
app.controller(
  'TeamListController',
  ['$scope', '$route', 'TeamsFactory', 'TeamFactory', '$location', function($scope, $route, TeamsFactory, TeamFactory, $location) {

    $scope.createNewTeam = function() {
      $location.path('/team-create/');
    };

    $scope.uploadFileToUrl = function(file) {
        var fd = new FormData();
        fd.append('file', file);
        $http.post("/rest/match/xml", fd, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        })
        .success(function(){
        })
        .error(function(){
        });
    };

    $scope.viewTeam = function(teamId) {
      $location.path('/team-view/' + teamId);
    };

    $scope.deleteTeam = function(teamId) {

      // Reset
      $scope.deleteWorked = false;
      $scope.deleteFailed = false;

      // Delete
      TeamFactory.delete({ id: teamId },
        function success() {
          $scope.deleteWorked = true;

          // Remove from screen
          $("#team-"+teamId).remove();
        },
        function error() {
          $scope.deleteFailed = true;
        }
      );
    }

    // Retrieve all the teams
    $scope.teams = TeamsFactory.query();
  }
]);

// Team creation controller
app.controller(
  'TeamCreateController',
  ['$scope', '$routeParams', 'TeamsFactory', '$location', function($scope, $routeParams, TeamsFactory, $location) {
    $scope.createTeam = function() {
      TeamsFactory.create($scope.team);
      $location.path('/team-list');
    };
    // Cancel action
    $scope.cancel = function() {
      $location.path('/team-list');
    };
  }
]);

// Matchs list controller
app.controller(
  'MatchListController',
  ['$scope', '$routeParams', 'MatchsFactory', '$location', function($scope, $routeParams, MatchsFactory, $location) {

    $scope.createMatch = function() {
      $location.path('/match-create/');
    };

    $scope.score = function(matchId) {
      $location.path('/match-score/' + matchId);
    }

    $scope.officials = function(matchId) {
      $location.path('/match-officials/' + matchId);
    };

    // Retrieve all the matchs
    $scope.matchs = MatchsFactory.query();
  }
]);

// Match view controller
app.controller(
  'OfficialViewController',
  ['$scope', '$routeParams', 'MatchFactory', '$location', function($scope, $routeParams, MatchFactory, $location) {

    // Update match
    $scope.updateMatch = function() {
    	MatchFactory.update($scope.match,
        function success() { $scope.updateWorked = true; },
        function error() { $scope.updateFailed = true; }
      );
      $location.path('/match-view/' + match.id);
    };

    // Delete match
    $scope.deleteMatch = function() {
    	MatchFactory.delete({ id: $routeParams.id },
        function success() {
          $scope.deleteWorked = true;
          $location.path('/match-list');
        },
        function error() {
          $scope.deleteFailed = true;
          $location.path('/match-view');
        }
      );
    };

    // Cancel view
    $scope.cancel = function() {
      $location.path('/official-list');
    };

    // Go get informations on the match
    $scope.match = MatchFactory.query({ id: $routeParams.id });
  }
]);

// Match creation controller
app.controller(
  'MatchController',
  ['$scope', '$routeParams', 'MatchsFactory', 'MatchFactory', '$location', function($scope, $routeParams, MatchsFactory, MatchFactory, $location) {

    $scope.updateScore = function() {
      MatchFactory.update($scope.match,
      function success(data) {
        $scope.updateScoreSuccess = true;
      },
      function error(data) {
        $scope.updateScoreError = true;
      });
    };

    $scope.cancel = function() {
      $location.path('/match-list');
    }

    $scope.match = MatchFactory.query({ id: $routeParams.id });
  }
]);

app.controller(
  'MatchCreateController',
  ['$scope', 'MatchsFactory', 'TeamsFactory', '$location', function($scope, MatchsFactory, TeamsFactory, $location) {

    $scope.create = function() {
      MatchsFactory.create($scope.match);
      $location.path('/match-list')
    };

    // Retrieve all the teams
    $scope.teams = TeamsFactory.query();
  }
]);


// Official controller
app.controller(
  'OfficialListController',
  ['$scope', 'OfficialsFactory', 'OfficialFactory', '$location', function($scope, OfficialsFactory, OfficialFactory, $location) {

    $scope.createOfficial = function() {
      $location.path('/official-create/');
    };

    $scope.editOfficial = function(officialId) {
      $location.path('/official-view/' + officialId);
    };

    $scope.deleteOfficial = function(officialId) {
      OfficialFactory.delete({ id: officialId });
      $scope.officials = OfficialsFactory.query();
    };

    // Retrieve all the officials
    $scope.officials = OfficialsFactory.query();
  }
]);

// Official arbitrate controller
app.controller(
	//TODO
);

// Official creation controller
app.controller(
  'OfficialCreateController',
  ['$scope', '$routeParams', 'OfficialsFactory', '$location', function($scope, $routeParams, OfficialsFactory, $location) {

    // Create user
    $scope.createNewOfficial = function() {
      PlayersFactory.create($scope.user);
      $location.path('/official-list');
    };

    // Cancel action
    $scope.cancel = function() {
      $location.path('/official-list');
    };

  }
]);
