import UIKit
import MobileCoreServices

class ShareViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Starte die Verarbeitung im Hintergrund
        DispatchQueue.global(qos: .userInitiated).async {
            self.processSharedFile()
        }
    }
    
    private func processSharedFile() {
        guard let extensionContext = self.extensionContext else {
            print("Fehler: Kein Extension-Kontext verfügbar.")
            completeExtension()
            return
        }
        
        // Verarbeite jede übergebene Datei
        for item in extensionContext.inputItems as? [NSExtensionItem] ?? [] {
            for attachment in item.attachments ?? [] {
                if attachment.hasItemConformingToTypeIdentifier(kUTTypeData as String) {
                    attachment.loadItem(forTypeIdentifier: kUTTypeData as String, options: nil) { data, error in
                        if let fileUrl = data as? URL {
                            // Speichere die Datei
                            self.saveToAppGroup(fileUrl)
                        } else {
                            print("Fehler beim Laden der Datei: \(String(describing: error))")
                        }
                        // Signalisiere den Abschluss der Verarbeitung
                        self.completeExtension()
                    }
                    return
                }
            }
        }
        
        // Falls keine Dateien verarbeitet wurden
        completeExtension()
    }
    
    private func saveToAppGroup(_ fileUrl: URL) {
        let fileManager = FileManager.default
        let appGroupContainer = fileManager.containerURL(forSecurityApplicationGroupIdentifier: "group.de.doubleslash.cmpprototype")
        guard let destinationUrl = appGroupContainer?.appendingPathComponent(fileUrl.lastPathComponent) else {
            print("Fehler: App Group Container nicht gefunden.")
            return
        }
        
        do {
            try fileManager.copyItem(at: fileUrl, to: destinationUrl)
            print("Datei erfolgreich gespeichert: \(destinationUrl)")
        } catch {
            print("Fehler beim Speichern der Datei: \(error)")
        }
    }
    
    private func completeExtension() {
        DispatchQueue.main.async {
            self.extensionContext?.completeRequest(returningItems: nil, completionHandler: nil)
        }
    }
}
