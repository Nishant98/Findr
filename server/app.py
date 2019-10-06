from flask import Flask, request ,send_from_directory,jsonify
import random
import socket
import json
import sqlite3
import hashlib
import re


ip = "http://192.168.0.103:8080"
description = "Lorem ipsum dolor sit amet, proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
app = Flask(__name__, static_url_path='' )


# USER table
conn = sqlite3.connect('database.db')
try:
	conn.execute('CREATE TABLE if not exists User ( email TEXT, password TEXT, city TEXT, pincode INTEGER(7), mobile INTEGER(11),name TEXT, PRIMARY KEY(email))')
	conn.close()
except:
	print("Error while creating User table")
	conn.close()

# Restaurant table
conn = sqlite3.connect('database.db')
try:
	conn.execute('CREATE TABLE if not exists Restaurant ( rid INTEGER, name TEXT, city TEXT, PRIMARY KEY(rid))')
	conn.close()
except :
	print("Error while creating Restaurant table")
	conn.close()

# Food table
conn = sqlite3.connect('database.db')
try:
	conn.execute('CREATE TABLE if not exists "food" ( `rid` INTEGER, `category` TEXT, `image` TEXT, `price` INTEGER, FOREIGN KEY(`rid`) REFERENCES `Restaurant`(`rid`) on delete cascade )')
	conn.close()
except:
	print("Error while creating food table")
	conn.close()

#Create table for food_order

conn = sqlite3.connect('database.db')
try:
	conn.execute('CREATE TABLE if not exists "orderloc" ( `email` TEXT, `rid` INTEGER, `totalprice` INTEGER, `time` DATETIME, FOREIGN KEY(`email`) REFERENCES `User`(`email`), FOREIGN KEY(`rid`) REFERENCES `Restaurant`(`rid`) on delete cascade )')
	conn.close()
except :
	print("Error while creating orderloc table")
	conn.close()

#wishlist table

conn = sqlite3.connect('database.db')
try:
	conn.execute('CREATE TABLE if not exists "wishlist" ( `email` TEXT, `rid` INTEGER, `category` TEXT, `image` TEXT, `ordered` INTEGER DEFAULT 0, FOREIGN KEY(`rid`) REFERENCES `Restaurant`(`rid`) on delete cascade, FOREIGN KEY(`email`) REFERENCES `User`(`email`) )')
	conn.close()
except :
	print("Error while creating wishlist table")
	conn.close()


#Route to IMAGES directory

@app.route('/images/<path:path>', methods=['GET','POST'])
def images(path):
	return send_from_directory('images',path)


#Route to delete ordered items
@app.route('/delOrdered',methods = ['POST','GET'])
def delOrdered():
	print("del")
	if request.method == 'POST':
		content = request.json
		email = content['email']
		restaurant_name = content['restaurant_name']
		category = content['category']
		rid = content['rid']
		image_name = content['image']


		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			cur.execute("DELETE from wishlist where email = ? and ordered=1 and image = ?",(email,image_name))
			conn.commit()
		return "Removed"

# Route for clearing order
@app.route('/removeOrder',methods = ['POST','GET'])
def removeOrder():
	if request.method == 'POST':
		content = request.json
		email = content['email']
		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			cur.execute('UPDATE wishlist set ordered=2 where email= ? and ordered = 1',(email,))
			conn.commit()
		return "Remove Ordered List"



#Route for LOGIN

@app.route('/login', methods = ['POST', 'GET'])
def login():
	if request.method == 'POST':
		content = request.json
		email = content['email']
		password = content['password']
		result = (hashlib.md5(password.encode())).hexdigest()
		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			cur.execute("SELECT * from User where email = ? and password = ?", (email, result))
			rows = cur.fetchall();
			if(rows):
				print(rows)
				return "1"
			else:
				return "0"


#Route for REGISTER
def check(password):
	lis_password=["!","@","#","$","%","*"]
	for i in password:
		if(i in lis_password):
			return 1
	return 0

@app.route('/register', methods = ['POST', 'GET'])
def register():
	if request.method == 'POST':
		content = request.json
		email= content['email']
		password= content['password']
		name = content['name']
		contact = content['contact']
		contact=int(contact)
		city = content['city']
		pincode = content['pincode']
		pincode = int(pincode)

		#check whether parameters are blank
		if(email=="" or password=="" or name=="" or city=="" or contact=="" or pincode==""):
			return "One of the parameters is blank."
		elif (len(password)<8 and check(password) == 0):
			# print("in elif")
			# print(check(password))
			# print(len(password))
			# print("condition",len(password)<8 and check(password) == 0)
			return "Invalid password"
		else:
			if not re.match(r"[^@]+@[^@]+\.[^@]+", email): #check format of email
				return "Invalid Email"
			else:
				result = (hashlib.md5(password.encode())).hexdigest()
				with sqlite3.connect("database.db") as conn:
					cur = conn.cursor()
					cur.execute("SELECT * FROM User where email = ?", (email,)) #check whether user is present or not
					rows = cur.fetchall();
					if(rows):
						return "User already exists."
					else:
						cur = conn.cursor()
						cur.execute("INSERT INTO User VALUES (?, ?, ?, ?, ?, ?)",(email, result, city, pincode, contact, name)) #insert
						conn.commit()
						return "1" #registration success


#Route to add items in Card View
@app.route('/addSwipe', methods=['GET','POST'])
def addSwipe():
	global ip
	restaurant = random.randrange(1,7)
	with sqlite3.connect("database.db") as conn:
		cur = conn.cursor()
		cur.execute("select name from Restaurant where rid=?",(restaurant,))
		 #check whether user is present or not
		rows = cur.fetchone()
		restaurant_name = rows[0]
	with sqlite3.connect("database.db") as conn:
		cur = conn.cursor()
		cur.execute("select category,image,price from food where rid=?",(restaurant,))
		 #check whether user is present or not
		rows = cur.fetchall();
		# print(rows[0][0])
		length = len(rows)
		#print("yejcbkwjdc",temp)
	path_parameters = random.randrange(0,length)
	# path = ip+","+restaurant_name+ "," +rows[path_parameters][0]+ "," +rows[path_parameters][1]+ "," +str(description)+","+rows[path_parameters][2]
	res = {'ip': ip,'restaurant_name':restaurant_name,'category':rows[path_parameters][0],'imgname':rows[path_parameters][1],'price':rows[path_parameters][2],'description':str(description),'rid':restaurant}
	# print(res)
	return jsonify(res)

#Route to SWIPE

@app.route('/swipe', methods=['GET','POST'])
def swipe():
	global ip
	content = request.json
	email  = content['email']
	restaurant_name = content['restaurant_name']
	category = content['category']

	image = content['image']
	rid = content['rid']
	rid = int(rid)
	swipe = content['swipe']
	if(swipe=="1"):
		#add in DB
		with sqlite3.connect("database.db") as con:
			cur = con.cursor()
			cur.execute('INSERT INTO wishlist (email,rid,category,image) values(?,?,?,?)',(email,rid,category,image))
			con.commit()
			print("inserted")
		return "1"
	else:
		#code for left swipe
		return "0"

# Route to display items in Wishlist
@app.route('/getWishlist', methods=['GET','POST'])
def getWishlist():
	if request.method == 'POST':
		content = request.json
		email= content['email']

		conn = sqlite3.connect('database.db')
		cur = conn.cursor()
		cur.execute('SELECT distinct Restaurant.name, wishlist.category, wishlist.image, wishlist.rid, price from wishlist, food, Restaurant where email = ? and ordered = 0 and wishlist.image = food.image and wishlist.category = food.category and Restaurant.rid = wishlist.rid',(email,))
		rows = cur.fetchall()
		t = []

		if(rows==[]):
			return "not found"

		else:
			for i in range(0, len(rows)):
				restaurant_name = rows[i][0]
				category = rows[i][1]
				image = rows[i][2]
				price = rows[i][4]
				rid = rows[i][3]

				res = {'restaurant_name':restaurant_name,'category':category,'image':image,'price':price,'rid':rid}
				t.append(res)
			# print(t)
			return jsonify(t)


@app.route('/order', methods=['GET','POST'])
def order():
	if request.method == 'POST':

		content = request.json
		restaurant_name = content['restaurant_name']
		category = content['category']
		email  = content['email']
		image = content['image']
		rid = content['rid']
		rid = int(rid)

		with sqlite3.connect("database.db") as con:
			cur = con.cursor()
			cur.execute('UPDATE wishlist set ordered=1 where email= ? and rid= ? and category = ? and image = ?',(email, rid, category ,image))
			con.commit()
		return "1"


# Route to display items in OrderCart

@app.route('/getOrdered', methods=['GET','POST'])
def getOrdered():
	if request.method == 'POST':
		content = request.json
		email= content['email']
		conn = sqlite3.connect('database.db')
		cur = conn.cursor()
		cur.execute('SELECT distinct Restaurant.name, wishlist.category, wishlist.image, wishlist.rid, price from wishlist, food, Restaurant where email = ? and ordered = 1 and wishlist.image = food.image and wishlist.category = food.category and Restaurant.rid = wishlist.rid',(email,))
		rows = cur.fetchall()
		t = []

		if(rows==[]):
			return "not found"

		else:
			for i in range(0, len(rows)):
				restaurant_name = rows[i][0]
				category = rows[i][1]
				image = rows[i][2]
				price = rows[i][4]
				rid = rows[i][3]
				res = {'restaurant_name':restaurant_name,'category':category,'image':image,'price':price,'rid':rid}
				t.append(res)
			# print(t)
			return jsonify(t)


# Route to display items in History
@app.route('/getHistory', methods=['GET','POST'])
def getHistory():
	if request.method == 'POST':
		content = request.json
		email= content['email']
		conn = sqlite3.connect('database.db')
		cur = conn.cursor()
		cur.execute('SELECT distinct Restaurant.name, wishlist.category, wishlist.image, wishlist.rid, price from wishlist, food, Restaurant where email = ? and ordered = 2 and wishlist.image = food.image and wishlist.category = food.category and Restaurant.rid = wishlist.rid',(email,))
		rows = cur.fetchall()
		t = []

		if(rows==[]):
			return "not found"

		else:
			for i in range(0, len(rows)):
				restaurant_name = rows[i][0]
				category = rows[i][1]
				image = rows[i][2]
				price = rows[i][4]
				rid = rows[i][3]
				res = {'restaurant_name':restaurant_name,'category':category,'image':image,'price':price,'rid':rid}
				t.append(res)
			return jsonify(t)

#Run server on local IP and port 8080
if __name__ == '__main__':
   app.run('0.0.0.0',8080,True)
