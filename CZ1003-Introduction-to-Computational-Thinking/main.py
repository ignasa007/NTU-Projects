import tkinter as tk
from tkinter import ttk
from tkinter import simpledialog, messagebox
from tkinter import *
import datetime
from datetime import *
from PIL import Image, ImageTk
import calendar
from tkcalendar import Calendar, DateEntry
import random

# Obtain menu from external file
entireMenu = []
menuFile = open('menu.txt','r')
for entry in menuFile:
    info = entry.split(',')
    entireMenu.append(info)
print(entireMenu)

now = datetime.now()
titleFont = ('Calibri Bold',30)
bodyFont = ('Calibri',14)
smallFont = ('Calibri',12)
menuFont = ('Lucida Console',14)
newDate = now.date()
newTime = now.time().strftime('%H%M')

root = Tk()
root.resizable(0,0)

# Importing external Images
NSCpic = ImageTk.PhotoImage(Image.open('assets/Welcome.png'))
AllStoresBG = ImageTk.PhotoImage(Image.open('assets/AllStoresBG.png'))
Miniwokpic = ImageTk.PhotoImage(Image.open('assets/MiniWokpic.png'))
ChickenRicepic = ImageTk.PhotoImage(Image.open('assets/ChickenRicepic.png'))
WesternFoodpic = ImageTk.PhotoImage(Image.open('assets/WesternFoodpic.png'))
IndianCuisinepic = ImageTk.PhotoImage(Image.open('assets/IndianCuisinepic.png'))
MalayBBQpic = ImageTk.PhotoImage(Image.open('assets/MalayBBQpic.png'))
DrinksStallpic = ImageTk.PhotoImage(Image.open('assets/DrinksStallpic.png'))

#Dictionary to link 'coding store name' and 'display store name' for each store
storeInfo = {'MiniWok':'Mini Wok', 'ChickenRice':'Chicken Rice', 'WesternFood':'Western Food', 'IndianCuisine':'Indian Cuisine', 'MalayBBQ':'Malay BBQ', 'DrinksStall':'Drinks Stall'}   

# Obtain operating hours from external file
rawOperatingHours=[]
operatingHoursFile = open('operating_hours.txt','r')
for entry in operatingHoursFile:
    info = entry.split(',')
    rawOperatingHours.append(info)
        
# North Spine Canteen Welcome Page
root.title('Welcome to North Spine Canteen!')
root.geometry('902x660')
root.wm_iconbitmap('assets/FoodIcon.ico')

def main_menu():

    # Background Image
    nscPage = tk.Label(root, image=NSCpic)
    nscPage.pack(side='top', fill='both', expand='yes')

    # Text and buttons that go on top of Background Image
    label = tk.Label(nscPage, text='Welcome to North Spine Canteen!', font=titleFont, bg='white')
    label.place(x=450, y=250, anchor='center')
    storesButton = tk.Button(nscPage, text='View all stores', font=bodyFont, width='50', command=lambda: [restore(),show_stores()])
    storesButton.place(x=451, y=400, anchor='center')
    exitButton = tk.Button(nscPage, text='Exit', font=bodyFont, width='50', bg='red', fg='white', command=root.destroy)
    exitButton.place(x=451, y=450, anchor='center')

main_menu()

# Function to remove current frame
def restore():
    for widgets in root.winfo_children():
        widgets.destroy()

# Function for change date/time window
def datetime_button():

    global tempNewDate,newDate, newTime, newHour, newMin
    datetimeWindow = tk.Toplevel(root)
    datetimeWindow.grab_set()
    datetimeWindow.title('Choose Another Date/Time')
    datetimeWindow.geometry('600x300')
    datetimeWindow.resizable(0,0)
    datetimeWindow.wm_iconbitmap('FoodIcon.ico')

    backImage = tk.Label(datetimeWindow, image= AllStoresBG)
    backImage.pack(side='top', fill='both', expand='yes')

    datetimeLabel = tk.Label(datetimeWindow, bg='DeepSkyBlue4',relief=RIDGE)
    datetimeLabel.place(relx= 0.04, rely= 0.17, relwidth= 0.67,relheight= 0.5)

    tempNewDate = newDate
    
    # Creating variable label text for date
    newDate_str = 'Date Chosen:   ' + newDate.strftime('%A, %d %B %Y')
    newDate_var = StringVar()
    newDate_var.set(newDate_str)

    # Creating date label and button
    dateLabel = tk.Label(datetimeWindow, textvariable= newDate_var, font = bodyFont, bg='DeepSkyBlue4',fg='white')
    dateLabel.place(relx= 0.08, rely= 0.2)

    dateButton = tk.Button(datetimeWindow, text= 'Change Date', font = bodyFont, bg='DeepSkyBlue4',fg='white', wraplength= 70, command=lambda:open_calendar())
    dateButton.place(relx= 0.74, rely= 0.17, relwidth = 0.22, relheight = 0.22)

    # Creation of calendar to choose date
    def open_calendar():

        global newDate, newTime, newHour, newMin
        calWindow = tk.Toplevel(root)
        calWindow.wm_iconbitmap('FoodIcon.ico')
        calWindow.title('Choose Date')
        calWindow.grab_set()
        cal = Calendar(calWindow, font=bodyFont, selectmode='day', cursor='hand1')
        cal.pack(fill = 'both', expand = True)
        
        calButton = tk.Button(calWindow, text='OK', font=bodyFont, width=10, command = lambda: [select_date(),calWindow.destroy()])
        calButton.pack()

        # Creating function to set new date
        def select_date():
            global tempNewDate
            tempNewDate = cal.selection_get() 
            newDate_str = 'Date Chosen:  ' + tempNewDate.strftime('%A, %d %B %Y')
            newDate_var.set(newDate_str)
               

    # Creating hour drop-list
    newHour = StringVar()
    newHour.set (12)
    hourDropList = tk.OptionMenu(datetimeWindow, newHour,'00','01','02','03','04','05','06','07','08','09',10,11,12,13,14,15,16,17,18,19,20,21,22,23)
    hourDropList.config(font=bodyFont, relief= FLAT, width=5, bg='SkyBlue')
    hourDropList.place(relx=0.28, rely=0.43)

    # Creating minute drop-list
    newMin = StringVar()
    newMin.set ('00')
    minDropList = tk.OptionMenu(datetimeWindow, newMin,'00','05',10,15,20,25,30,35,40,45,50,55)
    minDropList.config(font=bodyFont,relief= FLAT, width=5, bg='SkyBlue')
    minDropList.place(relx=0.5, rely=0.43) 

    # Creating time label
    timeChosenLabel = tk.Label(datetimeWindow, text= 'Time Chosen: ', font=bodyFont, bg='DeepSkyBlue4',fg='white')
    timeChosenLabel.place(relx=0.08, rely=0.45)
    colonLabel = tk.Label(datetimeWindow, text= ' : ', font= bodyFont, bg='DeepSkyBlue4',fg='white')
    colonLabel.place(relx=0.45, rely=0.45)  

    # Creating current time button
    nowButton = tk.Button(datetimeWindow, text= 'Select Current Date & Time', font=bodyFont,wraplength= 150, bg='DeepSkyBlue4', fg='white', command=lambda: now_datetime())
    nowButton.place(relx=0.74, rely=0.45, relwidth = 0.22, relheight = 0.22)

    def now_datetime():
        global tempNewDate, newHour, newMin, ID
        now = datetime.now()
        tempNewDate = now.date()
        newDate_str = 'Date Chosen:  ' + now.strftime('%A, %d %B %Y')
        newDate_var.set(newDate_str)
        newHour.set(now.strftime('%H'))
        newMin.set(now.strftime('%M'))
        print('New Date Selected:',tempNewDate.strftime('%A, %d %B %Y')) # For Debugging
        print('New Time Selected:',newTime) # For Debugging
    
    # Creating Cancel Button
    cancelButton = tk.Button(datetimeWindow, text= 'Cancel', font=bodyFont, bg='deep pink',fg='white', command=lambda: datetimeWindow.destroy())
    cancelButton.place(relx=0.04, rely=0.76, relwidth = 0.22, relheight = 0.2)
    
    # Creating Exit Button and Function
    confirmButton = tk.Button(datetimeWindow, text= 'Confirm', font=bodyFont, bg='forestgreen',fg='white', command=lambda: confirm_datetime())
    confirmButton.place(relx=0.74, rely=0.76, relwidth = 0.22, relheight = 0.2)

    def confirm_datetime():

        global tempNewDate, newDate, newTime, ID
        checkTime = str(newHour.get()) + str(newMin.get())

        # Check to prevent user from choosing an earlier date
        if tempNewDate < now.date():

            warningBox = tk.messagebox.showwarning('Invalid Input', 'You chose an earlier date!')
            datetimeWindow.lift()
            print('Wrong Date Selected:',tempNewDate.strftime('%A, %d %B %Y')) # For Debugging
        
        elif tempNewDate == now.date():
            
            if int(checkTime) < int(now.time().strftime('%H%M')): 
            
                warningBox = tk.messagebox.showwarning('Invalid Input', 'You chose an earlier time!')
                datetimeWindow.lift()
                print('Wrong Time Selected:',newTime) # For Debugging
            
            else:
            
                newTime = str(newHour.get()) + str(newMin.get())
                newDate = tempNewDate
                print('New Date Selected:',newDate.strftime('%A, %d %B %Y')) # For Debugging
                print('New Time Selected:',newTime) # For Debugging

                update_datetime()
                datetimeWindow.destroy()
                
                # If date/time is changed within a menu, the menu will be altered accordingly
                if ID == 'Mini Wok':
                    restore(),menu_page('MiniWok')
                elif ID == 'Chicken Rice':
                    restore(),menu_page('ChickenRice')
                elif ID == 'Western Food':
                    restore(),menu_page('WesternFood')
                elif ID == 'Indian Cuisine':
                    restore(),menu_page('IndianCuisine')
                elif ID == 'Malay BBQ':
                    restore(),menu_page('MalayBBQ')
                elif ID == 'Drinks Stall':
                    restore(),menu_page('DrinksStall')
                else:
                    restore(),show_stores()

        else:
            
            newTime = str(newHour.get()) + str(newMin.get())
            newDate = tempNewDate
            print('New Date Selected:',newDate.strftime('%A, %d %B %Y')) # For Debugging
            print('New Time Selected:',newTime) # For Debugging

            update_datetime()
            datetimeWindow.destroy()
            
            # If date/time is changed within menu, menu will be altered accordingly
            if ID == 'Mini Wok':
                restore(),menu_page('MiniWok')
            elif ID == 'Chicken Rice':
                restore(),menu_page('ChickenRice')
            elif ID == 'Western Food':
                restore(),menu_page('WesternFood')
            elif ID == 'Indian Cuisine':
                restore(),menu_page('IndianCuisine')
            elif ID == 'Malay BBQ':
                restore(),menu_page('MalayBBQ')
            elif ID == 'Drinks Stall':
                restore(),menu_page('DrinksStall')
            else:
                restore(),show_stores()

# Function to update Date and Time label on stores page
def update_datetime():
    global day_var, day_str, time_var, time_str, newDate, newTime
    day_str = 'Date  –  ' + newDate.strftime('%A, %d %B %Y')
    day_var = StringVar()
    day_var.set(day_str)
    time_str = 'Time  –  ' + '%s:%s' % (newTime[0:2],newTime[2:4])
    time_var = StringVar()
    time_var.set(time_str)
    
# Function to show all stores
def show_stores():

    global day_var,time_var,ID
    ID = 0
    allStores = tk.Label(root, image = AllStoresBG)
    allStores.pack(side='top', fill='both', expand='yes')

    update_datetime()

    infoLabel = tk.Label(allStores, bg='DeepSkyBlue4',relief=RIDGE)
    infoLabel.place(relx=0.03,rely=0.01,relwidth=0.36,relheight=0.1)
    dayLabel = tk.Label(allStores, textvariable= day_var, font=bodyFont, bg='DeepSkyBlue4',fg='white')
    dayLabel.place(relx=0.031,rely=0.017)
    timeLabel = tk.Label(allStores, textvariable= time_var, font=bodyFont, bg='DeepSkyBlue4',fg='white')
    timeLabel.place(relx=0.031,rely=0.061)

    luckyFood = tk.Button(allStores, text='Lucky Food!',font=bodyFont, bg='DeepSkyBlue4',fg='white', command=lambda: get_luckyfood())
    luckyFood.place(relx=0.52,rely=0.03,relwidth=0.16)

    changeDateTime = tk.Button(allStores, text='Choose another date/time',font=bodyFont, bg='DeepSkyBlue4',fg='white', command=lambda: [datetime_button()])
    changeDateTime.place(relx=0.7,rely=0.03,relwidth=0.27)

    backButton = tk.Button(allStores, text='Back',font=smallFont, bg='deep pink',fg='white', width='10', command=lambda:[restore(),main_menu()])
    backButton.pack(side='left',anchor=SW)
    
    exitButton = tk.Button(allStores, text='Exit',font=smallFont, bg='red',fg='white', width='10', command=lambda:root.destroy())
    exitButton.pack(side='right',anchor=SE)

    # Stall Buttons
    MiniWok = tk.Button(allStores, image=Miniwokpic,font=bodyFont, command= lambda: [restore(),menu_page('MiniWok')])
    MiniWok.place(relx=0.03,rely=0.13,relheight=0.25,relwidth=0.24)

    ChickenRice = tk.Button(allStores, image=ChickenRicepic, height='100', width='750',font=bodyFont,command= lambda: [restore(),menu_page('ChickenRice')])
    ChickenRice.place(relx=0.03,rely=0.41,relheight=0.25,relwidth=0.24)

    WesternFood = tk.Button(allStores, image=WesternFoodpic, height='100', width='750',font=bodyFont,command= lambda: [restore(),menu_page('WesternFood')])
    WesternFood.place(relx=0.03,rely=0.69,relheight=0.25,relwidth=0.24)

    IndianCuisine = tk.Button(allStores, image=IndianCuisinepic, height='100', width='750',font=bodyFont,command= lambda: [restore(),menu_page('IndianCuisine')])
    IndianCuisine.place(relx=0.52,rely=0.13,relheight=0.25,relwidth=0.24)

    MalayBBQ = tk.Button(allStores, image=MalayBBQpic, height='100', width='750',font=bodyFont,command= lambda: [restore(),menu_page('MalayBBQ')])
    MalayBBQ.place(relx=0.52,rely=0.41,relheight=0.25,relwidth=0.24)

    DrinksStall = tk.Button(allStores, image=DrinksStallpic, height='100', width='750',font=bodyFont,command= lambda: [restore(),menu_page('DrinksStall')])
    DrinksStall.place(relx=0.52,rely=0.69,relheight=0.25,relwidth=0.24)

    # Operating Hours Labels
    displayOperatingHours = create_operatinghours('MiniWok')  
    MiniWokOH = tk.Label(allStores, text=displayOperatingHours,font=smallFont, bg='white', justify=LEFT,anchor=NW, relief=RIDGE)
    MiniWokOH.place(relx=0.27,rely=0.13,relheight=0.25,relwidth=0.21)

    displayOperatingHours = create_operatinghours('ChickenRice')  
    ChickenRiceOH = tk.Label(allStores, text=displayOperatingHours,font=smallFont, bg='white', justify=LEFT,anchor=NW, relief=RIDGE)
    ChickenRiceOH.place(relx=0.27,rely=0.41,relheight=0.25,relwidth=0.21)

    displayOperatingHours = create_operatinghours('WesternFood')  
    WesternFoodOH = tk.Label(allStores, text=displayOperatingHours,font=smallFont, bg='white', justify=LEFT,anchor=NW, relief=RIDGE)
    WesternFoodOH.place(relx=0.27,rely=0.69,relheight=0.25,relwidth=0.21)

    displayOperatingHours = create_operatinghours('IndianCuisine')  
    IndianCuisineOH = tk.Label(allStores, text=displayOperatingHours,font=smallFont, bg='white', justify=LEFT,anchor=NW, relief=RIDGE)
    IndianCuisineOH.place(relx=0.76,rely=0.13,relheight=0.25,relwidth=0.21)

    displayOperatingHours = create_operatinghours('MalayBBQ')  
    MalayBBQOH = tk.Label(allStores, text=displayOperatingHours,font=smallFont, bg='white', justify=LEFT,anchor=NW, relief=RIDGE)
    MalayBBQOH.place(relx=0.76,rely=0.41,relheight=0.25,relwidth=0.21)

    displayOperatingHours = create_operatinghours('DrinksStall')  
    DrinksStallOH = tk.Label(allStores, text=displayOperatingHours,font=smallFont, bg='white', justify=LEFT,anchor=NW, relief=RIDGE)
    DrinksStallOH.place(relx=0.76,rely=0.69,relheight=0.25,relwidth=0.21)

    # Dictionary to link store name to its button
    storeButton = {'MiniWok':MiniWok,'ChickenRice':ChickenRice,'WesternFood':WesternFood,'IndianCuisine':IndianCuisine,'MalayBBQ':MalayBBQ,'DrinksStall':DrinksStall}
    
    # Convert time to integer for checks
    checkTime = int(newTime)

    # Disabling store button when closed
    for store in storeButton:
        check_open(store,storeButton[store],checkTime)
    
# Function to disable closed stores
def check_open(storename,button,checkTime):    
    for entry in rawOperatingHours:
        if entry[0] == storename and entry[1] == newDate.strftime('%A'):    #Find list corresponding to each store and to chosen day
            try:
                if checkTime < int(entry[2]) or checkTime >= int(entry[3]):     #Compares chosen time to store operating hours
                    button.config(state=DISABLED)   #CLOSED
                    print(storename,1,'time:',checkTime,'open:',int(entry[2]),'close:',int(entry[3])) #For Debugging
                else:
                    button.config(state=NORMAL)     #OPEN
                    print(storename,2,'time:',checkTime,'open:',int(entry[2]),'close:',int(entry[3])) #For Debugging
            except ValueError:                      #If entry[2] == 'Closed', there will be a ValueError
                button.config(state=DISABLED)       #CLOSED
                print(storename,3,'time:',checkTime,'store status:',entry[2],entry[3]) #For Debugging
                continue
    
# Function to calculate Queue time
def calc_queue(storename):

    QueueTime = {'MiniWok':2, 'ChickenRice':1, 'WesternFood':2.5, 'IndianCuisine':1.5, 'MalayBBQ':1.5, 'DrinksStall':1}

    while True:

        count_str = simpledialog.askstring('Queue Time', 'Enter the number of people in the queue') #variable count stores the number in type string
                
        try:
            count_int = int(count_str) #variable int_user_input stores the number in type integer
            if 0 <= count_int:
                time=(count_int*QueueTime[storename])
                print(count_int,'people *',QueueTime[storename],'min =',time,'min')
                message = 'The waiting will be approximately ' + str(time) + ' minutes.'
                messagebox.showinfo('Queue Time', message)
                break
            else:
                messagebox.showerror('Error','Error! Please enter a positive integer.')
                    
        except:
            if count_str == None:
                break
            else:
                messagebox.showerror('Error','Error! Please enter a positive integer.')

# Function to obtain menu from external file
def create_menu(Stall,Meal):

    display = ''
    for entry in entireMenu:
        if newDate.strftime('%A') == 'Monday':
            if Stall+Meal == 'WesternFoodRegular':
                if entry[0] == Stall+Meal or entry[0] == 'WesternFoodSpecial': # Check for Special Day
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('1: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
            else:
                if entry[0] == Stall+Meal:
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('2: ' + '{:30}'.format(entry[1]) + '   ' + entry[2])
        elif newDate.strftime('%A') == 'Tuesday':
            if Stall+Meal == 'MiniWokRegular':
                if entry[0] == Stall+Meal or entry[0] == 'MiniWokSpecial': # Check for Special Day
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('3: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
            else:
                if entry[0] == Stall+Meal:
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('4: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
        elif newDate.strftime('%A') == 'Wednesday':
            if Stall+Meal == 'DrinksStallRegular':
                if entry[0] == Stall+Meal or entry[0] == 'DrinksStallSpecial': # Check for Special Day
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('5: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
            else:
                if entry[0] == Stall+Meal:
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('6: ' + '{:30}'.format(entry[1]) + '   ' + entry[2])
        elif newDate.strftime('%A') == 'Thursday':
            if Stall+Meal == 'IndianCuisineRegular':
                if entry[0] == Stall+Meal or entry[0] == 'IndianCuisineSpecial': # Check for Special Day
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('7: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
            else:
                if entry[0] == Stall+Meal:
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('8: ' + '{:30}'.format(entry[1]) + '   ' + entry[2])
        elif newDate.strftime('%A') == 'Friday':
            if Stall+Meal == 'ChickenRiceRegular':
                if entry[0] == Stall+Meal or entry[0] == 'ChickenRiceSpecial': # Check for Special Day
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('9: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
            else:
                if entry[0] == Stall+Meal:
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('10: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
        elif newDate.strftime('%A') == 'Saturday':
            if Stall+Meal == 'MalayBBQRegular':
                if entry[0] == Stall+Meal or entry[0] == 'MalayBBQSpecial': # Check for Special Day
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('11: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
            else:
                if entry[0] == Stall+Meal:
                    display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                    print('12: ' + '{:30}'.format(entry[1]) + '   ' + entry[2]) # For Debugging
        else:
            if entry[0] == Stall+Meal:
                display += '{:30}'.format(entry[1]) + '$' + entry[2] + '\n'
                print('13: ' + '{:30}'.format(entry[1]) + '$' + entry[2] + '\n') # For Debugging
    return display

# Function to create menu page for each stall  
def menu_page(storename):

    global ID
    ID = storeInfo[storename]
    
    menuBG = ImageTk.PhotoImage(Image.open(f'assets/{storename}BG.png'))
    menu = tk.Label(root, image=menuBG)
    menu.pack(side='top', fill='both', expand='yes')
    menu.image = menuBG

    title = tk.Label(menu, text='Welcome to '+storeInfo[storename]+'!', font=titleFont, height='2', bg='gold', fg='black')
    title.pack(side='top',fill=X)

    menuType = tk.Label(menu, font=titleFont,bg='white') #To enter menu type later on
    menuType.place(relx=0.28,rely=0.28,relheight=0.09,relwidth=0.44)

    menuLabel = tk.Label(menu, font=menuFont,bg='white', anchor=N, justify=LEFT) #To enter menu text later on
    menuLabel.place(relx=0.28,rely=0.38,relheight=0.36,relwidth=0.44)

    backToStores = tk.Button(menu,text='Back to List of Stores', font=bodyFont, width='29', bg='hot pink', fg='black', command = lambda: [restore(),show_stores()])
    backToStores.pack(side='left', anchor=S)
    waitingTime = tk.Button(menu,text='Estimated Queue Time', font=bodyFont, width='29', bg='OliveDrab1', fg='black', command = lambda: calc_queue(storename))
    waitingTime.pack(side='left', anchor=S)
    anotherDate = tk.Button(menu, text='Choose Date and Time', font=bodyFont, width='29', bg='cyan', fg='black', command=lambda:datetime_button())
    anotherDate.pack(side='left', anchor=S) 

    # Convert time to integer for checks
    checkTime = int(newTime)
        
    # Check for requested time to show respective menu  
    for entry in rawOperatingHours:
        if entry[0] == storename and entry[1] == newDate.strftime('%A'):
            try:
                if int(entry[2])<= checkTime <= 1100:
                    menuType.config(text='Breakfast Menu')
                    displaymenu = create_menu(storename,'Morning')
                    menuLabel.config(text=displaymenu)
                elif 1100 < checkTime <= int(entry[3]):
                    menuType.config(text='Regular Menu')
                    displaymenu = create_menu(storename,'Regular')
                    menuLabel.config(text=displaymenu)
                else:
                    menuType.config(text='Sorry, we're closed.')
                    displayOperatingHours = menu_operatinghours(storename)
                    menuLabel.config(text=displayOperatingHours)
                    backToStores.pack(side='left',anchor=S)
                    waitingTime.destroy()
                    anotherDate.pack(side='right',anchor=S)              
            except:
                continue

    try:
        if '*' in displaymenu:
            specialLabel = tk.Label(menu,text='Special Menu', font=titleFont,bg='white')
            specialLabel.place(relx=0.28,rely=0.28,relheight=0.09,relwidth=0.44)
    except:
        pass

# Function to create operating hours info
def create_operatinghours(storename):
    displayOperatingHours='Operating Hours:\n'
    for entry in rawOperatingHours:
        if entry[0] == storename:
            if entry[3] != '':
                displayOperatingHours += entry[1] + ': ' + entry[2] + ' - ' + entry[3] + '\n'
            else:
                displayOperatingHours += entry[1] + ': ' + entry[2] + '\n'
    return displayOperatingHours       

# Function to create formatted operating hours info
def menu_operatinghours(storename):
    displayOperatingHours='Operating Hours:\n\n'
    for entry in rawOperatingHours:
        if entry[0] == storename:
            if entry[3] != '':
                displayOperatingHours += '{:20}'.format(entry[1] + ': ') + entry[2] + ' - ' + entry[3] + '\n'
            else:
                displayOperatingHours += '{:20}'.format(entry[1] + ': ') + entry[2] + '\n'
    return displayOperatingHours       


# Function for lucky food
def get_luckyfood():
    
    foodList = []
    for entry in entireMenu:
        if '*' not in entry[1]:
            foodList.append([entry[0],entry[1]])

    luckyFood = foodList[random.randint(0,61)]
    luckyMessage = 'Your lucky food today is: ' + luckyFood[1] + '!\n' + 'You can get this at: ' + storeInfo[luckyFood[0][:-7]]

    print('Your lucky food today is: ' + luckyFood[1])
    
    luckyFoodPopUp = tk.messagebox.showinfo('Lucky Food', luckyMessage)

root.mainloop()
