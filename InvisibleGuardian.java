import java.util.Arrays;

public class InvisibleGuardian {

    // ==========================================
    // 1. BYTECODE VERIFIER (The Gatekeeper)
    // ==========================================
    public static class BytecodeVerifier {
        public boolean verifyUpdate(byte[] incomingBytecode, String cryptographicSignature) {
            if (!isValidSignature(cryptographicSignature)) {
                System.out.println("[Verifier] SECURITY ALERT: Unauthorized Update Signature! Rejecting.");
                return false;
            }
            for (byte opcode : incomingBytecode) {
                if (opcode == 0x7F) { 
                    System.out.println("[Verifier] EXCEPTION: Malicious network outbound code detected!");
                    return false;
                }
            }
            System.out.println("[Verifier] Verification Passed: Code is safe and signed.");
            return true;
        }

        private boolean isValidSignature(String sig) {
            return "TRUSTED_SIGNATURE_KEY".equals(sig);
        }
    }

    // ==========================================
    // 2. AISecuritySandbox (The Isolated Chamber)
    // ==========================================
    public static class AISecuritySandbox {
        private byte[] currentModelWeights;

        public AISecuritySandbox(byte[] verifiedBytecode) {
            this.currentModelWeights = verifiedBytecode;
        }

        public boolean scanContentBeforeUpload(String fileName, byte[] fileData) {
            System.out.println("[Sandbox] Silently analyzing " + fileName + " in isolated memory...");
            for (byte b : fileData) {
                if (b == (byte) 0x99) { 
                    System.out.println("[Sandbox] Target Content Pattern Detected! Initializing silent block.");
                    return false; 
                }
            }
            return true; 
        }
    }

    // ==========================================
    // 3. ONE-WAY UPDATER (Data Ingestion Only)
    // ==========================================
    public static class OneWayUpdater {
        private final BytecodeVerifier verifier = new BytecodeVerifier();
        private AISecuritySandbox activeSandbox;

        public OneWayUpdater(byte[] initialWeights) {
            this.activeSandbox = new AISecuritySandbox(initialWeights);
        }

        public AISecuritySandbox getActiveSandbox() {
            return this.activeSandbox;
        }

        public void receiveUpdateOverTheAir(byte[] newUpdateBytes, String signature) {
            System.out.println("\n[Updater] Ingesting new binary rules updates...");
            if (verifier.verifyUpdate(newUpdateBytes, signature)) {
                this.activeSandbox = new AISecuritySandbox(newUpdateBytes);
                System.out.println("[Updater] System hot-swapped successfully inside the isolated chamber.");
            } else {
                System.out.println("[Updater] Update failed due to verification error.");
            }
        }
    }

    // ==========================================
    // MAIN SIMULATION RUNNER
    // ==========================================
    public static void main(String[] args) {
        System.out.println("=== INVISIBLE GUARDIAN ARCHITECTURE SIMULATION ===");
        
        byte[] baseRules = {0x01, 0x02, 0x03};
        OneWayUpdater coreSystem = new OneWayUpdater(baseRules);
        
        System.out.println("\n--- SCENARIO 1: Safe File Upload ---");
        byte[] safePhotoBytes = {0x10, 0x20, 0x30};
        boolean allowUpload1 = coreSystem.getActiveSandbox().scanContentBeforeUpload("family_photo.jpg", safePhotoBytes);
        System.out.println("Result to OS: " + (allowUpload1 ? "Upload Successful (Allowed)" : "Network Error (Blocked)"));

        System.out.println("\n--- SCENARIO 2: Illegal Content Upload ---");
        byte[] adultContentBytes = {0x10, (byte) 0x99, 0x30}; 
        boolean allowUpload2 = coreSystem.getActiveSandbox().scanContentBeforeUpload("restricted_media.mp4", adultContentBytes);
        System.out.println("Result to OS: " + (allowUpload2 ? "Upload Successful (Allowed)" : "Network Error (Blocked by Silent Daemon)"));

        System.out.println("\n--- SCENARIO 3: Genuine System Update ---");
        byte[] genuineUpdate = {0x01, 0x05, 0x06};
        coreSystem.receiveUpdateOverTheAir(genuineUpdate, "TRUSTED_SIGNATURE_KEY");

        System.out.println("\n--- SCENARIO 4: Attack Attempt ---");
        byte[] maliciousUpdate = {0x01, 0x7F, 0x06}; 
        coreSystem.receiveUpdateOverTheAir(maliciousUpdate, "FAKE_MALICIOUS_KEY");
    }
}
