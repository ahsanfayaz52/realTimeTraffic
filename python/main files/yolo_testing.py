import cv2
import numpy as np
import os
import sys


def postprocess(frame, outs):
    frameHeight = frame.shape[0]
    frameWidth = frame.shape[1]
    class_ids = []
    confidences = []
    boxes = []
    for out in outs:
        for detection in out:
            scores = detection[5:]

            class_id = np.argmax(scores)

            confidence = scores[class_id]

            if confidence > 0.5:
                center_x = int(detection[0] * frameWidth)
                center_y = int(detection[1] * frameHeight)
                w = int(detection[2] * frameWidth)
                h = int(detection[3] * frameHeight)
                x = int(center_x - w / 2)
                y = int(center_y - h / 2)

                boxes.append([x, y, w, h])
                confidences.append(float(confidence))
                class_ids.append(class_id)

    indexes = cv2.dnn.NMSBoxes(boxes, confidences, 0.5, 0.4)

    font = cv2.FONT_HERSHEY_PLAIN
    vehicles = {'car': 0,
                'bus': 0,
                'truck': 0,
                'motorbike': 0,

                }

    total_vehicles = 0
    for i in range(len(boxes)):
        if i in indexes:
            x, y, w, h = boxes[i]
            label = str(classes[class_ids[i]])
            for key, value in vehicles.items():
                if label == key:
                    vehicles[key] += 1
                    total_vehicles += 1

            color = colors[i]
            cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
            cv2.putText(frame, label, (x, y), font, 2, color, 3)

    heavy_vehicles = vehicles['truck'] + vehicles['bus']
    for key, value in vehicles.items():
        print(key + ' = ' + str(value))
    print("total vehicles = {}".format(total_vehicles))
    if total_vehicles >= 10 and (total_vehicles - heavy_vehicles) >= heavy_vehicles:
        print('recurrent congestion')
    elif total_vehicles >= 10 and (total_vehicles - heavy_vehicles) <= heavy_vehicles:
        print('non recurrent congestion')

    else:
        print('no congestion')


net = cv2.dnn.readNet("yolov3.weights", "yolov3.cfg")
classes = []
with open("coco.names", "r") as f:
    classes = [line.strip() for line in f.readlines()]
colors = np.random.uniform(0, 255, size=(len(classes), 3))






winName = 'DL OD with OpenCV'
cv2.namedWindow(winName, cv2.WINDOW_NORMAL)
cv2.resizeWindow(winName, 1000, 1000)

cap = cv2.VideoCapture('Mandi Morh.mp4')
count = 0

while cv2.waitKey(1) < 0:
    # get frame from video
    hasFrame, frame = cap.read()




    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]

    blob = cv2.dnn.blobFromImage(frame, 0.00392, (416, 416), (0, 0, 0), True, crop=False)

    # Set the input the the net
    net.setInput(blob)


    outs = net.forward( output_layers)

    postprocess(frame, outs)

    # show the image
    cv2.imshow(winName, frame)

    count += 10
    cap.set(1, count)












