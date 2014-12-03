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

/*
var services = angular.module('ngdemo.services', ['ngResource']);

services.factory('DummyFactory', function ($resource) {
    return $resource('/ngdemo/web/dummy', {}, {
        query: { method: 'GET', params: {}, isArray: false }
    })
});

services.factory('UsersFactory', function ($resource) {
    return $resource('/ngdemo/web/users', {}, {
        query: { method: 'GET', isArray: true },
        create: { method: 'POST' }
    })
});

services.factory('UserFactory', function ($resource) {
    return $resource('/ngdemo/web/users/:id', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {id: '@id'} },
        delete: { method: 'DELETE', params: {id: '@id'} }
    })
});
*/