import glob, os, inspect, sqlite3, random
name = "images"
filenames= os.listdir (name)
print(filenames)
t =[] 

for filename in filenames:
        t.append(os.listdir(name+"/"+filename))
print(t)

lst = []

for i in range(0, len(filenames)):
        print("folder filenames : ", filenames[i])
        for j in range(0, len(t[i])):
                print(os.path.dirname(os.path.abspath(inspect.getfile(inspect.currentframe()))))
                print("inside restaurant", t[i][j])
                os.chdir("images/"+filenames[i]+"/"+t[i][j])
                for file in glob.glob("*.jpg"):
                        with sqlite3.connect("../../../database1.db") as con:
                                cur = con.cursor()
                                cur.execute('INSERT INTO food values(?,?,?,?)',(int(filenames[i]),t[i][j],file,random.randrange(50,250,50)))
                                con.commit()
                os.chdir("..")
                os.chdir("..")
                os.chdir("..")
