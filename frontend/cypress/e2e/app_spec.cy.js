describe('React App End-to-End Tests', () => {
  beforeEach(() => {
    // Perform a login request
    cy.request('POST', 'http://localhost:8080/tokens', {
      username: 'admin@fontys.nl',
      password: Cypress.env('ADMIN_PASSWORD')
    }).then((response) => {
      // Check the response format
      console.log('Login response:', response);

      // Check if response contains token
      if (response.body && response.body.token) {
        // Convert token to string and set cookie
        const authToken = response.body.token.toString(); // Convert token to string
        cy.setCookie('authToken', authToken);

        // Alternatively, preserve the cookie for subsequent tests
        // Cypress.Cookies.preserveOnce('authToken');
      }
    })
    // Visit the homepage after login
    cy.visit('/');
  });

  it('should display the home page', () => {
    cy.contains('Welcome to TutorTalk');
  });

  it('should navigate to login page', () => {
    cy.contains('Login').click();
    cy.url().should('include', '/login');
  });

  it('should log in and navigate to the dashboard', () => {
    cy.contains('Login').click();
    cy.get('input[placeholder="Username"]').type('admin@fontys.nl');
    cy.get('input[placeholder="Password"]').type(Cypress.env('ADMIN_PASSWORD'));
    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/dashboard');
  });

  it('should navigate to find tutor page', () => {
    cy.contains("Find a tutor").click();
    cy.url().should('include', '/signup/learner');
  });

  it('should navigate to about page', () => {
    cy.contains('About').click();
    cy.url().should('include', '/about');
  });
});
