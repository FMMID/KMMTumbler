import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        KoinInitKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            let sdk = KotlinDependencies().getViewModel()
            ContentView(viewModel: .init(sdk: sdk))
        }
    }
}
