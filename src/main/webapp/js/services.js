'use strict';

/* Services */

/*
 http://docs.angularjs.org/api/ngResource.$resource

 Default ngResources are defined as

 'get':    {method:'GET'},
 'save':   {method:'POST'},
 'query':  {method:'GET', isArray:true},
 'remove': {method:'DELETE'},
 'delete': {method:'DELETE'}

 */

var services

var services = angular.module('tp4.services', ['ngResource']);

/**
 * Players
 */
services.factory('PlayersFactory', function ($resource) {
  return $resource('/rest/player', {}, {
    query:  { method: 'GET',    isArray: true },
    create: { method: 'POST' }
  })
});
services.factory('PlayerFactory', function($resource) {
  return $resource('/rest/player/:id', {}, {
    query : { method: 'GET' },
    update: { method: 'PUT',    params: { id: '@id' } },
    delete: { method: 'DELETE', params: { id: '@id' } }
  })
});

/**
 * Teams
 */
services.factory('TeamsFactory', function($resource) {
  return $resource('/rest/team', {}, {
    query: { method: 'GET', isArray: true },
    create: { method: 'POST' }
  })
});
services.factory('TeamFactory', function($resource) {
  return $resource('/rest/team/:id', {}, {
    query: { method: 'GET' },
    delete: { method: 'DELETE', params: { id: '@id' } }
  })
});
services.factory('TeamPlayerFactory', function($resource) {
  return $resource('/rest/team/:id/player', {}, {
    query: { method: 'GET', isArray: true },
    add:   { method: 'PUT', params: { id: '@id' } }
  })
});
services.factory('TeamXMLFactory', function($resource) {
  return $resource('/rest/team/:id/xml', {}, {
    query: { method: 'GET' },
  })
});

/**
 * Officials
 */
services.factory('OfficialsFactory', function($resource) {
  return $resource('/rest/official', {}, {
    query: { method: 'GET', isArray: true },
    create: { method: 'POST' }
  })
});
services.factory('OfficialFactory', function($resource) {
  return $resource('/rest/official/:id', {}, {
    query: { method: 'GET' }
  })
});

/**
 * Matchs
 */
services.factory('MatchsFactory', function($resource) {
  return $resource('/rest/match', {}, {
    query: { method: 'GET', isArray: true },
    create: { method: 'POST' }
  })
});
services.factory('MatchFactory', function($resource) {
  return $resource('/rest/match/:id', {}, {
    query: { method: 'GET' },
    update: { method: 'PUT',  params: { id: '@id' } }
  })
});
services.factory('MatchOfficialsFactory', function($resource) {
  return $resource('/rest/match/:id/official', {}, {
    query:  { method: 'GET', isArray: true },
    add:    { method: 'PUT', params: { id: '@id' } }, // To know which official to add
    remove: { method: 'DELETE', params: { id: '@id' } } // To know which official to remove
  })
});
