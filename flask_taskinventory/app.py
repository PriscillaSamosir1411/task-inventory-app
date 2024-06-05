from flask import Flask, jsonify, request 
from flask_cors import CORS #untuk mengaktifkan android bisa mengakses API
from flask_sqlalchemy import SQLAlchemy  # type: ignore
from sqlalchemy import desc  # type: ignore
from flask_migrate import Migrate
from datetime import datetime

 
app = Flask(__name__) 
CORS(app) 

def get_current_time_wib():
    return datetime.now()
 
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:@localhost/task_inventory' 
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False 

db = SQLAlchemy(app) 
 
 
class Tasks(db.Model): 
    __tablename__ = 'tasks' 
    id = db.Column(db.Integer, primary_key=True, autoincrement=True) 
    title = db.Column(db.String(255), nullable=False) 
    description = db.Column(db.String(255), nullable=False) 
    category = db.Column(db.String(255), nullable=False) 
    created_time = db.Column(db.DateTime(255), nullable=False, default=datetime.now) 
    finished_time = db.Column(db.DateTime(255), nullable=False, default="0") 
    duration = db.Column(db.String(255), nullable=False, default="0") 
    status = db.Column(db.String(255), nullable=False, default="New") 
 
    def to_dict(self): 
        return {'id':self.id,'title': self.title, 'description': self.description, 'category': self.category,  
        'created_time': self.created_time, 'finished_time': self.finished_time, 'duration': self.duration, 'status':self.status} 
 
@app.route('/') 
def home(): 
    return jsonify({'message': 'Hey'}) 

# add data 
@app.route('/tasks/add', methods=['POST']) 
def add_task(): 
    new_task = request.get_json() 
    print(new_task) 
    new_task_data = Tasks(title=new_task['title'], 
                        description=new_task['description'], 
                        category=new_task['category'])  
                        # created_time=new_task['created_time'],    
                        # status=new_task['status'])
                        # finished_time=new_task['finished_time'],
                        # duration=new_task['duration']) 
    db.session.add(new_task_data) 
    db.session.commit() 
    return jsonify({'message': 'Task added successfully'}) 
 
 
# get data (to show) 
@app.route('/tasks', methods=['GET']) 
def get_task(): 
    status = request.args.get('status') 
    category = request.args.get('category')     
    print('$$$$$$$$$$$$$$$$$$$$$$$$') 
    if status and category: 
        tasks = Tasks.query.filter_by(status = status, category = category) 
    else: 
        tasks = Tasks.query.all() 
    task_list = [data.to_dict() for data in tasks] 
    return jsonify(task_list) 

# Move status
@app.route('/task_data/<int:id>', methods=['PUT'])
def update_task_data(id):
    try:
        task_data = Tasks.query.get(id)
        if not task_data:
            return jsonify({'message': 'Task data not found'}), 404
            
        current_status = task_data.status

        if current_status == 'New':
            task_data.status = 'In Progress'
        elif current_status == 'In Progress':
            task_data.status = 'Done'
            task_data.finished_time = datetime.now()
            # Pastikan bahwa task_data.created_time adalah datetime
            if isinstance(task_data.created_time, str):
                task_data.created_time = datetime.strptime(task_data.created_time, '%Y-%m-%d %H:%M:%S.%f')
            # Pastikan bahwa task_data.finished_time adalah datetime
            if isinstance(task_data.finished_time, str):
                task_data.finished_time = datetime.strptime(task_data.finished_time, '%Y-%m-%d %H:%M:%S.%f')
            # Lakukan operasi pengurangan
            task_data.duration = (task_data.finished_time - task_data.created_time).seconds // 60

        db.session.commit()
        return jsonify({'message': 'Task data updated successfully'})
    except Exception as e:
        app.logger.error(f"Error updating task data: {e}")
        return jsonify({'message': 'Internal Server Error'}), 500


# Get data Deskripsi 
@app.route('/tasks/<int:task_id>', methods=['GET'])
def get_task_by_id(task_id):
    task = Tasks.query.get(task_id)
    if task is None:
        return jsonify({'error': 'Task not found'}), 404
    return jsonify(task.to_dict())


# # delete data? harusnya ga perlu. 
# @app.route('/tasks/<int:id>', methods=['DELETE']) 
# def delete_task(id): 
#     task_data = Tasks.query.get(id) 
#     if not task_data: 
#         return jsonify({'message': 'Task not found'}), 404 
 
#     db.session.delete(task_data) 
#     db.session.commit() 
#     return jsonify({'message': 'Task data deleted successfully'}) 
 
# filtered get: status == all, status == new, status == in progress, status == done, status == all AND category == 
# @app.route('/tasks/<int:id>', methods=['GET']) 
# def get_task_data_by_id(id): 
#     task_data = Tasks.query.get(id) 
#     if task_data: 
#         return jsonify(task_data.to_dict()) 
#     return jsonify({'message': 'Task data not found'}), 404 
 
# # move status 
# @app.route('/task_data/<int:id>', methods=['PUT']) 
# def update_task_data(id): 
#     task_data = Tasks.query.get(id) 
#     if not task_data: 
#         return jsonify({'message': 'Task data not found'}), 404 
 
#     data = request.get_json() 
#     if (data["status"] == "done"): 
#         task_data.finished_time = data['finished_time'] 
#         task_data.duration = data['duration'] 
#     task_data.status = data['status'] 
#     db.session.commit() 
#     return jsonify({'message': 'Task data updated successfully'}) 

migrate = Migrate(app, db)
 
 
if __name__ == '__main__': 
    # db.create_all() 
    app.run(debug=True)