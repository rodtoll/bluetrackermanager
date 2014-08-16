/**
 * Created by rodtoll on 8/12/14.
 */
angular.module('todoApp', [])
    .controller('TodoController', ['$scope', '$http', function($scope,$http) {

        $scope.todos = [
            {text:'learn angular', done:true},
            {text:'build an angular app', done:false}];

        $scope.addTodo = function() {
            $scope.todos.push({text:$scope.todoText, done:false});
            $scope.todoText = '';
            window.alert('Hi!');
            $http({method: 'GET', url: '/_ah/api/node/v1/node/', headers:{'x-troublex3-bluetracker-auth' : 'yo'}}).
                success(function(data, status, headers, config) {
                    window.alert(typeof data.items);
                    len = data.items.length;
                    window.alert(data.items.length);
                    for(var x = 0; x < len; x++) {
                        window.alert(data.items[x].nodeId);
                    }
                }).
                error(function(data, status, headers, config) {
                    window.alert('ERROR');
                });
            window.alert('There!');

        };

        $scope.remaining = function() {
            var count = 0;
            angular.forEach($scope.todos, function(todo) {
                count += todo.done ? 0 : 1;
            });
            return count;
        };

        $scope.archive = function() {
            var oldTodos = $scope.todos;
            $scope.todos = [];
            angular.forEach(oldTodos, function(todo) {
                if (!todo.done) $scope.todos.push(todo);
            });
        };
    }]);
