import io
from PIL import Image
from flask import request,jsonify,Flask

app = Flask(__name__)
    
@app.route("/prediction", methods=['POST'])
def predict():

    plantType = request.form['plantType']

    image = request.files['image'].read()
    image = Image.open(io.BytesIO(image))

    response = {
        'result': {
            'pestName': '파녹병',
            # 'pestName' : '정상_배추',
            'pestPercentage': 0.19971016
        }
    } 

    return jsonify(response)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)