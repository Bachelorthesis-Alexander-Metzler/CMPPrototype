import UIKit
import MobileCoreServices
import UniformTypeIdentifiers

class ShareViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Zugriff auf die geteilten Inhalte
        if let extensionItems = extensionContext?.inputItems as? [NSExtensionItem] {
            for item in extensionItems {
                if let attachments = item.attachments {
                    for attachment in attachments {
                        // Unterstützt alle Dateitypen
                        if attachment.hasItemConformingToTypeIdentifier(UTType.data.identifier) {
                            attachment.loadItem(forTypeIdentifier: UTType.data.identifier, options: nil) { [weak self] (data, error) in
                                if let fileURL = data as? URL {
                                    self?.handleSharedFile(fileURL: fileURL)
                                } else if let error = error {
                                    print("Fehler beim Laden der Datei: \(error)")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private func handleSharedFile(fileURL: URL) {
        DispatchQueue.global(qos: .background).async {
            self.saveFileToAppGroupContainer(fileURL: fileURL)

            // Navigation zur Haupt-App
            DispatchQueue.main.async {
                self.navigateToMainApp()
                self.extensionContext?.completeRequest(returningItems: nil, completionHandler: nil)
            }
        }
    }

    private func saveFileToAppGroupContainer(fileURL: URL) {
        let appGroupIdentifier = "group.de.doubleslash.cmpprototype"

        guard let containerURL = FileManager.default.containerURL(forSecurityApplicationGroupIdentifier: appGroupIdentifier) else {
            print("App Group Container konnte nicht gefunden werden")
            return
        }

        let destinationURL = containerURL.appendingPathComponent(fileURL.lastPathComponent)

        do {
            if FileManager.default.fileExists(atPath: destinationURL.path) {
                try FileManager.default.removeItem(at: destinationURL)
            }

            try FileManager.default.copyItem(at: fileURL, to: destinationURL)
            print("Datei erfolgreich gespeichert: \(destinationURL)")
        } catch {
            print("Fehler beim Speichern der Datei: \(error)")
        }
    }

    private func navigateToMainApp() {
        let urlScheme = "cmpprototype://"
        guard let url = URL(string: urlScheme) else {
            print("Ungültige URL")
            return
        }

        // Versuch, die Haupt-App zu öffnen
        DispatchQueue.main.async {
            var responder: UIResponder? = self
            while responder != nil {
                if let application = responder as? UIApplication {
                    application.open(url, options: [:], completionHandler: { success in
                        if success {
                            print("Haupt-App wurde erfolgreich geöffnet")
                        } else {
                            print("Fehler: Haupt-App konnte nicht geöffnet werden")
                        }
                    })
                    return
                }
                responder = responder?.next
            }
            print("UIApplication konnte nicht gefunden werden")
        }
    }
}
