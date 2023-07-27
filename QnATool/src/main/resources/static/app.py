from flask import Flask, request, jsonify
import bardapi
import os

app = Flask(__name__)

token = 'PleaseEnterThe_Secure-1PSID'

# Specify the absolute path to the index.html file.
index_html_path = os.path.abspath('C:\\Users\\P7113583\\Desktop\\QnATool\\src\\main\\resources\\templates\\index.html')


def read_index_html():
    # Read the content of index.html file and return it.
    with open(index_html_path, 'r') as file:
        return file.read()


@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        question = request.form.get('question')

        # Send an API request and get a response.
        response = bardapi.core.Bard(token).get_answer(question)

        # Get the content from the response.
        content = response['content']

        return jsonify({'content': content})

    # Read the index.html content and return it as a response.
    return read_index_html()


if __name__ == '__main__':
    app.run(debug=True)
