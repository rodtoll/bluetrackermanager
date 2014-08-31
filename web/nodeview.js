/**
 * Created by rodtoll on 8/22/14.
 */
/**
 * Created by rodtoll on 8/12/14.
 */
angular.module('dashboardApp', [])
    .controller('NodeController', ['$scope', '$http', '$timeout', function($scope,$http,$timeout) {

        $scope.devices = [];
        $scope.friendlyDevices = [];

        $scope.nodes = []

        newNode = new Object();
        newNode.nodeId = 'macbookpro';
        newNode.deviceList = [];
        $scope.nodes.push(newNode);
        newNode = new Object();
        newNode.nodeId = 'pb1-basement';
        newNode.deviceList = [];
        $scope.nodes.push(newNode);
        newNode = new Object();
        newNode.nodeId = 'pb3-playroom';
        newNode.deviceList = [];
        $scope.nodes.push(newNode);
        newNode = new Object();
        newNode.nodeId = 'pbmaster-office';
        newNode.deviceList = [];
        $scope.nodes.push(newNode);
        newNode = new Object();
        newNode.nodeId = 'rainbow-frontdoor';
        newNode.deviceList = [];
        $scope.nodes.push(newNode);
        newNode = new Object();
        newNode.nodeId = 'smoky-garage';
        newNode.deviceList = [];
        $scope.nodes.push(newNode);
        newNode = new Object();
        newNode.nodeId = 'white-tvroom';
        newNode.deviceList = [];
        $scope.nodes.push(newNode);

        $scope.updateDevices = function() {
            $http({method: 'GET', url: '/_ah/api/tracker/v1/device/', headers: {'x-troublex3-bluetracker-auth': 'yo'}}).
                success(function (data, status, headers, config) {
                    newDeviceList = new Object();
                    newFriendlyDeviceList = new Object();

                    for(nodeIndex = 0 ; nodeIndex < $scope.nodes.length; nodeIndex++) {
                        node = $scope.nodes[nodeIndex];
                        node.deviceList = [];
                    }

                    for (x = 0; x < data.items.length; x++) {
                        device = data.items[x];
                        newDeviceList[data.items[x].address] = device;
                        newFriendlyDeviceList[data.items[x].friendlyName] = device;
                        for(nodeIndex = 0 ; nodeIndex < $scope.nodes.length; nodeIndex++) {
                            node = $scope.nodes[nodeIndex];
                            if(device.seenMap[node.nodeId] != undefined) {
                                //if(node.deviceList.indexOf(device) == -1 ) {
                                    node.deviceList.push(device);
                                //}
                            }
                        }
                    }
                    $scope.devices = newDeviceList;
                    $scope.friendlyDevices = newFriendlyDeviceList;
                    $timeout($scope.updateDevices, 2000);
                }).
                error(function (data, status, headers, config) {
                    $timeout($scope.updateDevices, 2000);
                });


        };
        $scope.getSecondsSinceUpdateFromDevice = function(device) {
            return $scope.getTimeDelta(device.lastSeen);
        };

        $scope.getTimeDelta = function(dateToCheck) {
            currentDate = new Date();
            tmpDate = new Date(dateToCheck);

            dateDelta = (currentDate.getTime() - tmpDate.getTime()) / 1000;

            return dateDelta;
        };

        $scope.updateDevices();
    }]);
