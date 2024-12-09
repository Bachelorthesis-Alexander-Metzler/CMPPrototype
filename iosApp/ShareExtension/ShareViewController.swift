import UIKit
import Social
import MobileCoreServices

class ShareViewController: SLComposeServiceViewController {
    
    override func isContentValid() -> Bool {
        // Hier kannst du validieren, ob der Inhalt korrekt ist
        return true
    }
    
    override func presentationAnimationDidFinish() {
        // Direkt verarbeiten, ohne das Popup anzuzeigen
        DispatchQueue.global(qos: .background).async {
            self.handleSharedFile()
            
            // Abschluss sicherstellen
            DispatchQueue.main.async {
                self.completeExtension()
            }
        }
    }
    
    override func didSelectPost() {
        // Diese Methode wird nicht verwendet, da wir das Popup überspringen
    }
    
    private func handleSharedFile() {
        guard let extensionContext = self.extensionContext else {
            print("Fehler: Kein Extension-Kontext verfügbar.")
            return
        }
        
        for item in extensionContext.inputItems as? [NSExtensionItem] ?? [] {
            for attachment in item.attachments ?? [] {
                if attachment.hasItemConformingToTypeIdentifier(kUTTypeData as String) {
                    attachment.loadItem(forTypeIdentifier: kUTTypeData as String, options: nil) { data, error in
                        if let fileUrl = data as? URL {
                            self.saveToAppGroup(fileUrl)
                        } else {
                            print("Fehler beim Laden der Datei: \(String(describing: error))")
                        }
                    }
                }
            }
        }
    }
    
    private func saveToAppGroup(_ fileUrl: URL) {
        let fileManager = FileManager.default
        let appGroupContainer = fileManager.containerURL(forSecurityApplicationGroupIdentifier: "group.de.doubleslash.cmpprototype")
        let destinationUrl = appGroupContainer?.appendingPathComponent(fileUrl.lastPathComponent)
        
        do {
            try fileManager.copyItem(at: fileUrl, to: destinationUrl!)
            print("Datei erfolgreich gespeichert: \(destinationUrl?.absoluteString ?? "")")
        } catch {
            print("Fehler beim Speichern der Datei: \(error)")
        }
    }
    
    private func completeExtension() {
        // Signalisiere der Host-App, dass die Verarbeitung abgeschlossen ist
        self.extensionContext?.completeRequest(returningItems: nil, completionHandler: nil)
    }
}
