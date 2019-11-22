/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PersonDetailsComponentsPage, PersonDetailsDeleteDialog, PersonDetailsUpdatePage } from './person-details.page-object';

const expect = chai.expect;

describe('PersonDetails e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let personDetailsUpdatePage: PersonDetailsUpdatePage;
  let personDetailsComponentsPage: PersonDetailsComponentsPage;
  let personDetailsDeleteDialog: PersonDetailsDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PersonDetails', async () => {
    await navBarPage.goToEntity('person-details');
    personDetailsComponentsPage = new PersonDetailsComponentsPage();
    await browser.wait(ec.visibilityOf(personDetailsComponentsPage.title), 5000);
    expect(await personDetailsComponentsPage.getTitle()).to.eq('Person Details');
  });

  it('should load create PersonDetails page', async () => {
    await personDetailsComponentsPage.clickOnCreateButton();
    personDetailsUpdatePage = new PersonDetailsUpdatePage();
    expect(await personDetailsUpdatePage.getPageTitle()).to.eq('Create or edit a Person Details');
    await personDetailsUpdatePage.cancel();
  });

  it('should create and save PersonDetails', async () => {
    const nbButtonsBeforeCreate = await personDetailsComponentsPage.countDeleteButtons();

    await personDetailsComponentsPage.clickOnCreateButton();
    await promise.all([
      personDetailsUpdatePage.setPersonNameInput('personName'),
      personDetailsUpdatePage.setPersonIdInput('5'),
      personDetailsUpdatePage.setPersonAddressInput('personAddress')
    ]);
    expect(await personDetailsUpdatePage.getPersonNameInput()).to.eq('personName', 'Expected PersonName value to be equals to personName');
    expect(await personDetailsUpdatePage.getPersonIdInput()).to.eq('5', 'Expected personId value to be equals to 5');
    expect(await personDetailsUpdatePage.getPersonAddressInput()).to.eq(
      'personAddress',
      'Expected PersonAddress value to be equals to personAddress'
    );
    await personDetailsUpdatePage.save();
    expect(await personDetailsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await personDetailsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last PersonDetails', async () => {
    const nbButtonsBeforeDelete = await personDetailsComponentsPage.countDeleteButtons();
    await personDetailsComponentsPage.clickOnLastDeleteButton();

    personDetailsDeleteDialog = new PersonDetailsDeleteDialog();
    expect(await personDetailsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Person Details?');
    await personDetailsDeleteDialog.clickOnConfirmButton();

    expect(await personDetailsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
