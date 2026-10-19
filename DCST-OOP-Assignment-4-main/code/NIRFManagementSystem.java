import java.io.*;
import java.lang.reflect.*;
import java.util.*;

enum ActivityType { ACADEMIC, NON_ACADEMIC }
enum MemberRole { STUDENT, TEACHING_STAFF, NON_TEACHING_STAFF }

class DepartmentRecord {
    public String departmentName;
    protected String rankingCycle;
    private double departmentalBudget;

    public DepartmentRecord(String departmentName, String rankingCycle, double departmentalBudget) {
        this.departmentName = departmentName;
        this.rankingCycle = rankingCycle;
        this.departmentalBudget = departmentalBudget;
    }

    public double getDepartmentalBudget() { return this.departmentalBudget; }
    public void setDepartmentalBudget(double departmentalBudget) {
        if (departmentalBudget >= 0) this.departmentalBudget = departmentalBudget;
    }

    public void displayInternalDetails() {
        System.out.println("  [Inside Class] Dept: " + departmentName + " | Cycle: " + rankingCycle + " | Budget: INR " + departmentalBudget);
    }
}

class DCSTSpecificRecord extends DepartmentRecord {
    public DCSTSpecificRecord(String cycle, double budget) {
        super("DCST, Goa University", cycle, budget);
    }
    public void displayProtectedAccess() {
        System.out.println("  [Subclass Protected Access] Cycle: " + this.rankingCycle);
    }
}

interface Reportable { void generateReport(); }
interface AuditLoggable {
    void logAuditTrail();
    default void complianceNotice() {
        System.out.println("  AuditLoggable: Complies with NIRF 2024-2026 reporting guidelines.");
    }
}

abstract class DepartmentMember implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private static int totalMemberCount = 0;
    protected String id, name;
    protected MemberRole role;

    public DepartmentMember(String id, String name, MemberRole role) {
        this.id = id; this.name = name; this.role = role;
        totalMemberCount++;
    }

    public static void displayTotalMembers() {
        System.out.println("  [Static Counter] Total DCST Members Registered: " + totalMemberCount);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public MemberRole getRole() { return role; }

    public abstract void printRoleSpecificDetails();

    @Override
    public Object clone() throws CloneNotSupportedException { return super.clone(); }
}

class Student extends DepartmentMember implements Reportable, AuditLoggable {
    private String rollNo, programEnrolled;
    private List<String> enrolledCourses;

    public Student(String id, String name, String rollNo, String programEnrolled) {
        super(id, name, MemberRole.STUDENT);
        this.rollNo = rollNo;
        this.programEnrolled = programEnrolled;
        this.enrolledCourses = new ArrayList<>();
    }

    public void enrollCourse(String courseName) { enrolledCourses.add(courseName); }
    public List<String> getEnrolledCourses() { return enrolledCourses; }

    @Override
    public void printRoleSpecificDetails() {
        System.out.println("  Student: " + name + " (Roll: " + rollNo + ") | Program: " + programEnrolled);
    }
    @Override
    public void generateReport() {
        System.out.println("  [NIRF Report] Student ID: " + id + ", Name: " + name + ", Program: " + programEnrolled);
    }
    @Override
    public void logAuditTrail() {
        System.out.println("  [Audit] Logged student academic record entry for ID: " + id);
    }
    public Student deepClone() {
        Student copy = new Student(this.id, this.name, this.rollNo, this.programEnrolled);
        copy.enrolledCourses = new ArrayList<>(this.enrolledCourses);
        return copy;
    }
}

class TeachingStaff extends DepartmentMember implements Reportable, AuditLoggable {
    private String designation, specialization;

    public TeachingStaff(String id, String name, String designation, String specialization) {
        super(id, name, MemberRole.TEACHING_STAFF);
        this.designation = designation; this.specialization = specialization;
    }
    @Override
    public void printRoleSpecificDetails() {
        System.out.println("  Teaching Faculty: " + name + " | " + designation + " | Spec: " + specialization);
    }
    @Override
    public void generateReport() {
        System.out.println("  [NIRF Report] Faculty: " + name + ", Designation: " + designation + ", Area: " + specialization);
    }
    @Override
    public void logAuditTrail() {
        System.out.println("  [Audit] Logged teaching workload and publication data for: " + name);
    }
}

class NonTeachingStaff extends DepartmentMember implements Reportable {
    private String dutyAssignment;

    public NonTeachingStaff(String id, String name, String dutyAssignment) {
        super(id, name, MemberRole.NON_TEACHING_STAFF);
        this.dutyAssignment = dutyAssignment;
    }
    @Override
    public void printRoleSpecificDetails() {
        System.out.println("  Non-Teaching Staff: " + name + " | Duty: " + dutyAssignment);
    }
    @Override
    public void generateReport() {
        System.out.println("  [NIRF Report] Staff: " + name + ", Role: " + dutyAssignment);
    }
}

class InvalidNIRFDataException extends Exception {
    public InvalidNIRFDataException(String message) { super(message); }
}

class Activity {
    private String activityName;
    private ActivityType type;
    private int year;

    public Activity(String activityName, ActivityType type, int year) {
        this.activityName = activityName; this.type = type; this.year = year;
    }
    public void displayActivityDetails() {
        System.out.println("  - " + activityName + " (" + type + ", Year: " + year + ")");
    }
    public void displayActivityDetails(String remarks) {
        System.out.println("  - " + activityName + " (" + type + ", Year: " + year + ") -> Remarks: " + remarks);
    }
}

class DataRepository<T> {
    private List<T> records = new ArrayList<>();
    public void addRecord(T item) { records.add(item); }
    public List<T> getAllRecords() { return records; }
    public static <E> void printGenericElements(List<E> elements) {
        for (E element : elements) System.out.println("    * " + element);
    }
}

class NIRFSubmissionPortal {
    public void submitReport(Reportable item) {
        System.out.print("  [Portal Message Passing] Dispatching to NIRF: ");
        item.generateReport();
    }
}

public class NIRFManagementSystem {
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   GOA UNIVERSITY - DEPARTMENT OF COMPUTER SCIENCE & TECHNOLOGY (DCST)");
        System.out.println("                 NIRF DATA SUBMISSION & COMPLIANCE SYSTEM");
        System.out.println("================================================================================\n");

        System.out.println("--- [1] Object Referencing & Constructors ---");
        Student s1 = new Student("S101", "Aakash Naik", "2101", "MCA");
        Student s2 = s1;
        System.out.println("Original Student 1 Name: " + s1.getName());
        s2.setName("Aakash V. Naik (Updated via Ref)");
        System.out.println("After mutating via ref s2 -> s1.getName(): " + s1.getName());
        System.out.println("Are s1 and s2 pointing to identical memory? " + (s1 == s2) + "\n");

        System.out.println("--- [2] Static Attributes, Methods & Enums ---");
        TeachingStaff t1 = new TeachingStaff("T201", "Dr. V. V. Kamat", "Professor", "Data Structures, AI");
        NonTeachingStaff nt1 = new NonTeachingStaff("NT301", "Mr. Ramesh Gaonkar", "Lab Assistant");
        DepartmentMember.displayTotalMembers();
        System.out.println("Enum constant: " + MemberRole.TEACHING_STAFF + " | " + ActivityType.ACADEMIC + "\n");

        System.out.println("--- [3] Multiple Inheritance (Interface Implementation) ---");
        s1.generateReport();
        s1.logAuditTrail();
        s1.complianceNotice();
        System.out.println();

        System.out.println("--- [4] Access Modifiers Demonstration ---");
        DepartmentRecord baseDept = new DepartmentRecord("DCST", "NIRF-2026", 5000000.00);
        baseDept.displayInternalDetails();
        System.out.println("  [Outside Class] Public departmentName: " + baseDept.departmentName);
        System.out.println("  [Outside Class via Getter] Private budget: " + baseDept.getDepartmentalBudget());
        DCSTSpecificRecord subRec = new DCSTSpecificRecord("NIRF-2026", 6200000.00);
        subRec.displayProtectedAccess();
        System.out.println();

        System.out.println("--- [5] Abstract Classes & Interfaces ---");
        DepartmentMember[] members = { s1, t1, nt1 };
        for (DepartmentMember member : members) member.printRoleSpecificDetails();
        System.out.println();

        System.out.println("--- [6] Class as a Type & Message Passing ---");
        NIRFSubmissionPortal portal = new NIRFSubmissionPortal();
        portal.submitReport(s1);
        portal.submitReport(t1);
        portal.submitReport(nt1);
        System.out.println();

        System.out.println("--- [7] Polymorphism (Overriding & Overloading) ---");
        Activity act1 = new Activity("Plateaunia (Annual Cultural Fest)", ActivityType.NON_ACADEMIC, 2024);
        Activity act2 = new Activity("SEA (Semester End Assessment)", ActivityType.ACADEMIC, 2025);
        act1.displayActivityDetails();
        act2.displayActivityDetails("Mandatory University Final Examination");
        System.out.println();

        System.out.println("--- [8] Generics & Collection Frameworks ---");
        List<String> academicActivities = Arrays.asList("ISA", "Midpoint Evaluation", "SEA", "Study-Tour", "Placements");
        List<String> nonAcademicActivities = Arrays.asList("InfoFest", "Plateaunia", "Play-on Sports Fest");
        Set<String> programsOffered = new HashSet<>(Arrays.asList("MCA", "M.Sc. Data Science", "M.Sc. AI", "Ph.D."));
        Queue<String> pendingQueue = new LinkedList<>();
        pendingQueue.offer("Compile Teaching Staff publications");
        Map<String, String> courseMap = new HashMap<>();
        courseMap.put("CST-101", "Design and Analysis of Algorithms");
        courseMap.put("CST-102", "Machine Learning & Deep Neural Networks");

        System.out.println("  * Academic Activities (List): " + academicActivities);
        System.out.println("  * Non-Academic Activities (List): " + nonAcademicActivities);
        System.out.println("  * Programs Offered (Set): " + programsOffered);
        System.out.println("  * Pending NIRF Queue Item: " + pendingQueue.poll());
        System.out.println("  * DCST Courses/Subjects (Map): " + courseMap);
        System.out.println();

        System.out.println("--- [9] Exception Handling Demonstration ---");
        try {
            validateIntake(-5);
        } catch (InvalidNIRFDataException e) {
            System.err.println("  [Caught Custom Exception] " + e.getMessage());
        } finally {
            System.out.println("  [Finally Block] Executed NIRF sanitation and cleanup.");
        }
        System.out.println();

        System.out.println("--- [10] Reflection, Persistence, I/O & Cloning ---");
        System.out.println("A. RTTI & Reflection: Class = " + s1.getClass().getName());
        Method[] methods = s1.getClass().getDeclaredMethods();
        for (int i = 0; i < Math.min(methods.length, 3); i++) {
            System.out.println("    - " + methods[i].getName());
        }

        System.out.println("\nB. Shallow vs Deep Cloning:");
        Student originalStudent = new Student("S105", "Tanvi Prabhu", "2105", "M.Sc. Data Science");
        originalStudent.enrollCourse("Data Mining");
        try {
            Student shallowCloned = (Student) originalStudent.clone();
            Student deepCloned = originalStudent.deepClone();
            originalStudent.getEnrolledCourses().add("NLP");
            System.out.println("  Original Courses : " + originalStudent.getEnrolledCourses());
            System.out.println("  Shallow Cloned   : " + shallowCloned.getEnrolledCourses() + " (Shares reference)");
            System.out.println("  Deep Cloned      : " + deepCloned.getEnrolledCourses() + " (Independent copy)");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        System.out.println("\nC. Object Serialization & File I/O:");
        String filename = "nirf_backup.ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(t1);
            System.out.println("  Serialized TeachingStaff object to '" + filename + "'.");
        } catch (IOException e) { e.printStackTrace(); }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            TeachingStaff deserialized = (TeachingStaff) ois.readObject();
            System.out.println("  Deserialized object read from file: " + deserialized.getName());
        } catch (Exception e) { e.printStackTrace(); }

        System.out.println("\n================================================================================");
        System.out.println("   NIRF PORTAL DATA SUBMISSION CYCLE SUCCESSFULLY SIMULATED");
        System.out.println("================================================================================");
    }

    public static void validateIntake(int intake) throws InvalidNIRFDataException {
        if (intake < 0) throw new InvalidNIRFDataException("Intake cannot be negative: " + intake);
    }
}
