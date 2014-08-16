/**
 * Created by rodtoll on 8/12/14.
 */
angular.module('dashboardApp', [])
    .controller('DashboardController', ['$scope', '$http', '$timeout', function($scope,$http,$timeout) {

        $scope.devices = [];
        $scope.friendlyDevices = [];

        $scope.update = function() {
            $http({method: 'GET', url: '/_ah/api/devices/v1/devices/', headers: {'x-troublex3-bluetracker-auth': 'yo'}}).
                success(function (data, status, headers, config) {
                    newDeviceList = new Object();
                    newFriendlyDeviceList = new Object();
                    for(x = 0; x < data.items.length;x++) {
                        newDeviceList[data.items[x].address] = data.items[x];
                        newFriendlyDeviceList[data.items[x].friendlyName] = data.items[x];
                    }
                    $scope.devices = newDeviceList;
                    $scope.friendlyDevices = newFriendlyDeviceList;
                    $timeout($scope.update, 2000);
                }).
                error(function (data, status, headers, config) {
                    window.alert('ERROR retrieving device list');
                    $timeout($scope.update, 2000);
                });
        };

        $scope.getSecondsSinceUpdateFromDevice = function(device) {

            currentDate = new Date();
            tmpDate = new Date(device.lastSeen);

            dateDelta = (currentDate.getTime() - tmpDate.getTime()) / 1000;

            return dateDelta;
        };

        $scope.getPresentStateFromDevice = function(device) {
            timeDeltaInSeconds = $scope.getSecondsSinceUpdateFromDevice(device);
            if(timeDeltaInSeconds > 66) {
                return false;
            } else {
                return true;
            }
        };

        $scope.getPresentStateFromDeviceName = function(deviceName) {
            if(deviceName in $scope.devices) {
                device = $scope.devices[deviceName];
                return $scope.getPresentStateFromDevice(device);
            } else if(deviceName in $scope.friendlyDevices) {
                device = $scope.friendlyDevices[deviceName];
                return $scope.getPresentStateFromDevice(device);
            } else {
                return false;
            }
        };

        $scope.getUserPresenceFromDeviceNames = function(phone_device_name, car_device_name) {
            if($scope.getPresentStateFromDeviceName(phone_device_name)) {
                return 'present';
            } else if($scope.getPresentStateFromDeviceName(car_device_name)) {
                return 'nearby';
            } else {
                return 'out';
            }
        };

        $scope.getLastReadingFromDeviceName = function(deviceName) {
            if(deviceName in $scope.devices) {
                device = $scope.devices[deviceName];
                if(device.lastReading != null) {
                    return device.lastReading.signalStrength;
                }
            }
            return -100000;
        }

        $scope.getDeviceLocationFromDeviceName = function(deviceName) {
            lastReading = $scope.getLastReadingFromDeviceName(deviceName);
            if(lastReading == -100000) {
                return "not available";
            } else if(lastReading == 0) {
                return "out";
            } else if(lastReading == 1) {
                return "home";
            } else if(lastReading == 2) {
                return "work";
            } else {
                return "unknown";
            }
        }

        $scope.update();
    }]);
