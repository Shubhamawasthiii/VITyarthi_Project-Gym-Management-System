import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

// --- Models (Data Classes) ---

class Member {
    private int id;
    private String name;
    private String contact;

    public Member(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public String toCSV() { return id + "," + name + "," + contact; }
    public String toString() { return "ID: " + id + " | " + name + " (" + contact + ")"; }
}

class Trainer {
    private int id;
    private String name;
    private String specialty;

    public Trainer(int id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public String toCSV() { return id + "," + name + "," + specialty; }
    public String toString() { return "ID: " + id + " | " + name + " [" + specialty + "]"; }
}

class Plan {
    private int id;
    private String name;
    private double price;

    public Plan(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public String toCSV() { return id + "," + name + "," + price; }
    public String toString() { return "ID: " + id + " | " + name + " - Rs." + price; }
}

class Subscription {
    private int id;
    private int memberId;
    private int planId;
    private int trainerId;
    private String date;

    public Subscription(int id, int memberId, int planId, int trainerId, String date) {
        this.id = id;
        this.memberId = memberId;
        this.planId = planId;
        this.trainerId = trainerId;
        this.date = date;
    }

    public int getId() { return id; }

    public String toCSV() { return id + "," + memberId + "," + planId + "," + trainerId + "," + date; }
    public String toString() {
        String trainerStr = (trainerId == 0) ? "No Trainer" : "Trainer ID: " + trainerId;
        return "Sub ID: " + id + " | Member ID: " + memberId + " | Plan ID: " + planId + " | " + trainerStr;
    }
}

// --- File Handler (Saves/Loads Data) ---

class FileHandler {
    public static void save(String filename, String data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.println(data);
        } catch (IOException e) { System.out.println("Error saving to " + filename); }
    }

    public static List<String> load(String filename) {
        List<String> lines = new ArrayList<>();
        File f = new File(filename);
        if (!f.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) { System.out.println("Error reading " + filename); }
        return lines;
    }
}

// --- Service Layer (Logic) ---

class GymService {
    private List<Member> members = new ArrayList<>();
    private List<Trainer> trainers = new ArrayList<>();
    private List<Plan> plans = new ArrayList<>();
    private List<Subscription> subs = new ArrayList<>();
    
    // ID counters
    private int nextMemId = 1, nextTrainId = 1, nextPlanId = 1, nextSubId = 1;

    public GymService() {
        loadData();
    }

    // Loads all data from text files at startup
    private void loadData() {
        for (String s : FileHandler.load("members.txt")) {
            String[] p = s.split(",");
            if(p.length == 3) {
                int id = Integer.parseInt(p[0]);
                members.add(new Member(id, p[1], p[2]));
                if(id >= nextMemId) nextMemId = id + 1;
            }
        }
        for (String s : FileHandler.load("trainers.txt")) {
            String[] p = s.split(",");
            if(p.length == 3) {
                int id = Integer.parseInt(p[0]);
                trainers.add(new Trainer(id, p[1], p[2]));
                if(id >= nextTrainId) nextTrainId = id + 1;
            }
        }
        for (String s : FileHandler.load("plans.txt")) {
            String[] p = s.split(",");
            if(p.length == 3) {
                int id = Integer.parseInt(p[0]);
                plans.add(new Plan(id, p[1], Double.parseDouble(p[2])));
                if(id >= nextPlanId) nextPlanId = id + 1;
            }
        }
        for (String s : FileHandler.load("subs.txt")) {
            String[] p = s.split(",");
            if(p.length == 5) {
                int id = Integer.parseInt(p[0]);
                subs.add(new Subscription(id, Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3]), p[4]));
                if(id >= nextSubId) nextSubId = id + 1;
            }
        }
    }

    public void addMember(String name, String contact) {
        Member m = new Member(nextMemId++, name, contact);
        members.add(m);
        FileHandler.save("members.txt", m.toCSV());
        System.out.println(">> Success: Member Added [" + m.getId() + "]");
    }

    public void addTrainer(String name, String spec) {
        Trainer t = new Trainer(nextTrainId++, name, spec);
        trainers.add(t);
        FileHandler.save("trainers.txt", t.toCSV());
        System.out.println(">> Success: Trainer Added [" + t.getId() + "]");
    }

    public void addPlan(String name, double price) {
        Plan p = new Plan(nextPlanId++, name, price);
        plans.add(p);
        FileHandler.save("plans.txt", p.toCSV());
        System.out.println(">> Success: Plan Created [" + p.getId() + "]");
    }

    public void createSubscription(int memId, int planId, int trainId) {
        // Validation using Streams
        boolean memExists = members.stream().anyMatch(m -> m.getId() == memId);
        boolean planExists = plans.stream().anyMatch(p -> p.getId() == planId);
        boolean trainExists = (trainId == 0) || trainers.stream().anyMatch(t -> t.getId() == trainId);

        if (memExists && planExists && trainExists) {
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Subscription s = new Subscription(nextSubId++, memId, planId, trainId, date);
            subs.add(s);
            FileHandler.save("subs.txt", s.toCSV());
            System.out.println(">> Success: Subscription Active! ID: " + s.getId()); 
        } else {
            System.out.println(">> Error: Invalid IDs provided.");
        }
    }

    public void viewMembers() {
        System.out.println("\n--- MEMBERS ---");
        if(members.isEmpty()) System.out.println("None found.");
        for(Member m : members) System.out.println(m);
    }

    public void viewTrainers() {
        System.out.println("\n--- TRAINERS ---");
        if(trainers.isEmpty()) System.out.println("None found.");
        for(Trainer t : trainers) System.out.println(t);
    }

    public void viewPlans() {
        System.out.println("\n--- PLANS ---");
        if(plans.isEmpty()) System.out.println("None found.");
        for(Plan p : plans) System.out.println(p);
    }
    
    public void viewSubs() {
        System.out.println("\n--- SUBSCRIPTIONS ---");
        if(subs.isEmpty()) System.out.println("None found.");
        for(Subscription s : subs) System.out.println(s);
    }
}

// --- Main Application ---

class GymSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GymService service = new GymService();

        System.out.println("=== GYM MANAGEMENT SYSTEM v1.0 ===");

        while(true) {
            System.out.println("\n1. Add Member");
            System.out.println("2. Add Trainer");
            System.out.println("3. Create Plan");
            System.out.println("4. New Subscription");
            System.out.println("5. View All Data");
            System.out.println("6. Exit");
            System.out.print("Select: ");

            try {
                int ch = Integer.parseInt(sc.nextLine());
                switch(ch) {
                    case 1:
                        System.out.print("Name: "); String mn = sc.nextLine();
                        System.out.print("Contact: "); String mc = sc.nextLine();
                        service.addMember(mn, mc);
                        break;
                    case 2:
                        System.out.print("Name: "); String tn = sc.nextLine();
                        System.out.print("Specialty: "); String ts = sc.nextLine();
                        service.addTrainer(tn, ts);
                        break;
                    case 3:
                        System.out.print("Plan Name: "); String pn = sc.nextLine();
                        System.out.print("Price: "); double pp = Double.parseDouble(sc.nextLine());
                        service.addPlan(pn, pp);
                        break;
                    case 4:
                        System.out.print("Member ID: "); int mid = Integer.parseInt(sc.nextLine());
                        System.out.print("Plan ID: "); int pid = Integer.parseInt(sc.nextLine());
                        System.out.print("Trainer ID (0 for none): "); int tid = Integer.parseInt(sc.nextLine());
                        service.createSubscription(mid, pid, tid);
                        break;
                    case 5:
                        service.viewMembers();
                        service.viewTrainers();
                        service.viewPlans();
                        service.viewSubs();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        System.exit(0);
                    default: System.out.println("Invalid Option.");
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid Input.");
            }
        }
    }
}