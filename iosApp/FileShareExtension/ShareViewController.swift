import UIKit
import UniformTypeIdentifiers

class ShareViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        handleSharedFile()
    }

    private func handleSharedFile() {
        DispatchQueue.global(qos: .userInitiated).async { [weak self] in
            guard let self = self else { return }
            
            guard let extensionContext = self.extensionContext else { return }

            for item in extensionContext.inputItems {
                guard let inputItem = item as? NSExtensionItem else { continue }
                guard let attachments = inputItem.attachments else { continue }

                for provider in attachments {
                    if provider.hasItemConformingToTypeIdentifier(UTType.data.identifier) {
                        provider.loadItem(forTypeIdentifier: UTType.data.identifier, options: nil) { (item, error) in
                            guard error == nil else {
                                NSLog("Error loading shared item: \(error!)")
                                return
                            }
                            if let fileURL = item as? URL {
                                self.saveFileToAppGroup(fileURL: fileURL)
                            }
                        }
                    }
                }
            }

            DispatchQueue.main.async {
                self.extensionContext?.completeRequest(returningItems: nil, completionHandler: nil)
            }
        }
    }

    private func saveFileToAppGroup(fileURL: URL) {
        let fileManager = FileManager.default
        if let appGroupURL = fileManager.containerURL(forSecurityApplicationGroupIdentifier: "group.de.doubleslash.cmpprototype") {
            let destinationURL = appGroupURL.appendingPathComponent(fileURL.lastPathComponent)
            do {
                try fileManager.copyItem(at: fileURL, to: destinationURL)
                NSLog("File saved to App Group: \(destinationURL)")
            } catch {
                NSLog("Error saving file to App Group: \(error)")
            }
        }
    }
}
