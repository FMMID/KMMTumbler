import SwiftUI
import shared

@main
struct iOSApp: App {
    
    let sdk = SDKTumbler(databaseDriveFactory: DatabaseDriveFactory())
    
	var body: some Scene {
		WindowGroup {
            ContentView(viewModel: .init(sdk: sdk))
		}
	}
}
