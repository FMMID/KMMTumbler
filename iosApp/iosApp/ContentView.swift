import SwiftUI
import shared

struct ContentView: View {
    @State var authorization = "Loading..."
    @ObservedObject private(set) var viewModel: ViewModel
    @State var isLoaderVisible = false
    @State var responseBody = "Empty"
    @State var isShowWebView = true
    
    func laodAuthorizationUrl(){
        self.viewModel.sdk.authorization() { result, error in
            if let result = result {
                switch result {
                case is AuthorizationStatus.Success :
                    self.viewModel.successAuth.send(true)
                case is AuthorizationStatus.Failure :
                    self.authorization = result.value
                default:
                    return
                }
            }else if let error = error {
                authorization = "Error: \(error)"
            }
        }
    }

    func getUserImages() {
        self.viewModel.sdk.getUserImages(){ result, error in
            if let result = result {
                responseBody = result.description
                isShowWebView = false
                print(responseBody)
            }else if let error = error {
                responseBody = "Error: \(error)"
            }
        }
    }

    func loadUserTokens(code:String){
        self.viewModel.sdk.getTokenUser(code: code) { result, error in
            if let result = result {
                self.viewModel.successAuth.send(result.boolValue)
            } else if let error = error {
                responseBody = "Error: \(error)"
            }

        }
    }
    
    var body: some View {
        Text(responseBody)
            .onReceive(self.viewModel.authorizationCode.receive(on: RunLoop.main)){ value in
                loadUserTokens(code: value)
            }.onReceive(self.viewModel.successAuth.receive(on: RunLoop.main)){ value in
                getUserImages()
            }
        ZStack {
            VStack(spacing: 0) {
                if (self.isShowWebView){
                    WebNavigationView(viewModel: viewModel)
                    WebView(
                        type: .public,
                        url: authorization,
                        viewModel: viewModel)
                } else {
                    WebNavigationView(viewModel: viewModel).hidden()
                    WebView(
                        type: .public,
                        url: authorization,
                        viewModel: viewModel).hidden()
                }
            }
            if isLoaderVisible {
                LoaderView()
            }
        }.onAppear(){
            laodAuthorizationUrl()
        }
        .onReceive(
            self.viewModel.isLoaderVisible.receive(on: RunLoop.main)
        ) { value in self.isLoaderVisible = value }
    }
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
