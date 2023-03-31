package STARS;

import java.util.ArrayList;
import java.io.*;

/**
 * Used to read from and write to the database
 * @author Group 3
 */

public class DatabaseManager {

    /**
     * Used to initialize the course list in CourseMgr
     * @return ArrayList of courses to be stored in CourseMgr
     */

    public static ArrayList<Course> initializeCmgr()
    {
        File file = new File("courses.dat");
        if(file.exists())
        {
            return (ArrayList<Course>)readSerializedObject(file);
        }
        return new ArrayList<Course>();
    }

    /**
     * Used to initialize the student list in StudentMgr
     * @return ArrayList of students to be stored in StudentMgr
     */

    public static ArrayList<Student> initializeSmgr()
    {
        File file = new File("students.dat");
        if(file.exists())
        {
            return (ArrayList<Student>)readSerializedObject(file);
        }
        return new ArrayList<Student>();
    }

    /**
     * Called every time a user logs out, updates the database with all changes made.
     */

    public static void updateDatabase()
    {
        ArrayList<Student> students = StudentMgr.getStudentList();
        ArrayList<Course> courses = CourseMgr.getCourseList();
        File f1 = new File("courses.dat");
        File f2 = new File("students.dat");
        try {
            f1.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            f2.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        writeSerializedObject(f1, courses);
        writeSerializedObject(f2, students);

    }

    /**
     * Reads a serialized object from file.
     * @param file - file to be read from
     * @return deserialized object
     */

    private static Object readSerializedObject(File file) {
        Object pDetails = null;
        FileInputStream fis;
        ObjectInputStream in;
        try {
            fis = new FileInputStream(file);
            in = new ObjectInputStream(fis);
            pDetails = in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return pDetails;
    }

    /**
     * Writes an object in serialized form to an external file
     * @param file - file to write to
     * @param o - object to be serialized
     */
    private static void writeSerializedObject(File file, Object o) {
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(o);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
