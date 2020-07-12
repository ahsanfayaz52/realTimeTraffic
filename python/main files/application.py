import eventlet
eventlet.monkey_patch()
import sys
import os
import cv2
from flask_socketio import SocketIO, emit
from flask import Flask
from yolo_video import yolo
from faster_rcnn import accident_detection
from findCongestion import findCongestion
from threading import Thread, Event

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
app.config['DEBUG'] = True

#turn the flask app into a socketio app
socketio = SocketIO(app,logger=True,engineio_logger=True)
thread = Thread()
Flag = 0

def realTimeTraffic (city,route):
    global Flag
    Flag = 0

    video = cv2.VideoCapture('Camera/' + city + "/" + route+'.mp4')
    count = 0
    os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
    count = 0

    while True:
        if Flag == 1:
            cv2.destroyAllWindows()
            Flag = 0
            break
        ret, frame = video.read()
        frame2 = frame
        yimage, vehicles, total_vehicles, heavy_vehicles = yolo(img = frame2)
        rcnn_prediction, rimage = accident_detection(image=frame)

        accident = False

        if rcnn_prediction[0] != 0:
            accident = True

        result = findCongestion(total_vehicles, heavy_vehicles, accident)
        dsize = (800, 600)

        output = cv2.resize(yimage, dsize)
        cv2.imshow('Real Time Traffic Monitoring', output)
        count += 10
        video.set(1,count)
        if cv2.waitKey(1) == ord('q'):
            print("hi")

        data = str(result[0]) + ',' + str(result[1]) + ',' + str(total_vehicles) + ',' + str(
            heavy_vehicles) + ',' + str(accident) + ',' + str(vehicles['car']) + ',' + str(
            vehicles['truck']) + ',' + str(vehicles['bus']) + ',' + str(vehicles['motorbike']) + ',' + str(
            result[2]) + ',' + str(result[3])

        if not socketio.sleep():
            socketio.emit('congestion report', {'data': data}, namespace='/test', Broadcast=False)


        socketio.sleep(3)

@app.route('/')
@socketio.on('congestion report', namespace='/test')
def find_congestion(params):
    # need visibility of the global thread object
    global thread

    print('Client connected')
    city_exist = 0
    route_exist = 0
    data = params.split('&')
    city = data [0]
    route = data[1]
    print(city)
    print(route)
    CWD_PATH = os.getcwd()

    PATH_TO_Camera = os.path.join(CWD_PATH, 'Camera')

    path_to_city = os.path.dirname(os.path.realpath(PATH_TO_Camera))

    for root, dirs, files in os.walk(path_to_city):
        for dir in dirs:

            if dir.startswith(city):
                city_exist = 1

                path_to_route = os.path.dirname(os.path.realpath(dir))

    if city_exist == 1:

        for root, dirs, files in os.walk(path_to_route):
            for file in files:

                if file.startswith(route):
                    route_exist = 1
    if (route_exist == 0):
        socketio.emit('congestion report', {'data': "Sorry no camera available on that route"}, namespace='/test')
    else:
        if not thread.isAlive():
            thread = socketio.start_background_task(realTimeTraffic(city,route))

@socketio.on('testConnection', namespace='/test')
def test_connect( ):
    socketio.emit('testConnection', {'data': "connected"}, namespace='/test')

@socketio.on('disconnect', namespace='/test')
def test_disconnect( ):
    global Flag
    Flag = 1
    cv2.destroyAllWindows()
    print('Client disconnected')

if __name__ == '__main__':
    socketio.run(app,debug=True,host='0.0.0.0' )


