package ca.teyssedre.mailer;

enum TaskStatus {

    None(0),
    Started(1),
    Running(5),
    Terminated(2),
    Pending(3),
    Cancel(4);

    private final int code;

    TaskStatus(int i) {
        this.code = i;
    }

    protected int getCode() {
        return code;
    }

    protected static TaskStatus parse(int code) {
        switch (code) {
            case 1:
                return Started;
            case 2:
                return Terminated;
            case 3:
                return Pending;
            case 4:
                return Cancel;
            case 5:
                return Running;
            case 0:
            default:
                return None;
        }
    }
}
